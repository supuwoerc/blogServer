package cn.lookup.sanye.utils;

import cn.lookup.sanye.config.security.JWTConfig;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.impl.SysUserDetailsService;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 21:16
 * @Desc: JwtToken工具类
 */
@Data
@Slf4j
@Component
public class JWTTokenUtil {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysUserDetailsService sysUserDetailsService;
    private static JWTTokenUtil jwtTokenUtil;
    //处理时间的格式化对象
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String CLAIM_KEY_ID = "jti";  //账户id
    private static final String CLAIM_KEY_USERNAME = "sub";   //账户名
    private static final String CLAIM_KEY_AUTHORITIES = "authorities"; //权限
    private static final String CLAIM_KEY_IP_ADDRESS = "ip"; //登录ip
    private static final String CLAIM_KEY_EXPIRATION = "expiration"; //过期时间
    private static final String CLAIM_KEY_REFRESH_TIME = "refreshTime"; //刷新时间
    @PostConstruct
    private void init(){
        jwtTokenUtil = this;
        jwtTokenUtil.redisUtil = redisUtil;
        jwtTokenUtil.sysUserDetailsService = sysUserDetailsService;
        log.info("注入工具类和service，让静态方法可用");
    }
    /**
     * 生成jwt-token的方法
     *
     * @param sysUserDetails 系统用户实例
     * @return token
     */
    public static String createAccessToken(SysUserDetails sysUserDetails) {
        LocalDateTime localDateTime = LocalDateTime.now();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ID, sysUserDetails.getId());
        claims.put(CLAIM_KEY_USERNAME, sysUserDetails.getUsername());
        claims.put(CLAIM_KEY_AUTHORITIES, sysUserDetails.getAuthorities());
        claims.put(CLAIM_KEY_IP_ADDRESS, sysUserDetails.getIp());
        claims.put(CLAIM_KEY_EXPIRATION,dateTimeFormatter.format(localDateTime.plusSeconds(JWTConfig.expiration/1000)));
        claims.put(CLAIM_KEY_REFRESH_TIME, dateTimeFormatter.format(localDateTime.plusSeconds(JWTConfig.refreshTime/1000)));
        String token = Jwts.builder()
                .setClaims(claims)  // 主体信息
                .setIssuedAt(new Date())   //签发时间
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration))//过期时间
                .signWith(SignatureAlgorithm.HS512, JWTConfig.secret)  //加密算法和秘钥
                .compact();
        return JWTConfig.tokenPrefix + token;
    }

    /**
     * 解析token获取用户实例信息
     *
     * @param token token字符串
     * @return 用户实例
     */
    public static SysUserDetails parseAccessToken(String token) {
        SysUserDetails sysUserDetails = null;
        if (!StringUtils.isEmpty(token)) {
            try {
                // 去除JWT前缀
                String subToken = token.substring(JWTConfig.tokenPrefix.length());
                //解析token
                Claims claims = Jwts.parser().setSigningKey(JWTConfig.secret).parseClaimsJws(subToken).getBody();
                sysUserDetails = new SysUserDetails();
                sysUserDetails.setId(Long.valueOf(claims.getId()));
                sysUserDetails.setUsername(claims.getSubject());
                sysUserDetails.setIp((String) claims.get(CLAIM_KEY_IP_ADDRESS));
                sysUserDetails.setExpiration((String) claims.get(CLAIM_KEY_EXPIRATION));
                sysUserDetails.setRefreshTime((String) claims.get(CLAIM_KEY_REFRESH_TIME));
                //从jwt中解析出角色
                ArrayList<LinkedHashMap<String,String>> authoritiesFromToken = (ArrayList<LinkedHashMap<String, String>>) claims.get(CLAIM_KEY_AUTHORITIES);
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                for (LinkedHashMap<String, String> roleLinkedHashMap : authoritiesFromToken) {
                    String roleName = roleLinkedHashMap.get("authority");
                    authorities.add(new SimpleGrantedAuthority(roleName));
                }
                sysUserDetails.setAuthorities(authorities);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("jwt解析失败",e.getCause());
            }
        }
        return sysUserDetails;
    }
    /**
     * 刷新Token
     *
     * @param oldToken 过期但未超过刷新时间的Token
     * @return
     */
    public static String refreshAccessToken(String oldToken,String ip) {
        String username = JWTTokenUtil.getUserNameByToken(oldToken);
        SysUserDetails sysUserDetails = (SysUserDetails) jwtTokenUtil.sysUserDetailsService.loadUserByUsername(username);
        sysUserDetails.setIp(ip);
        return createAccessToken(sysUserDetails);
    }

    /**
     * 将token存放到redis里面,同时映射用户名和token的对应关系
     * @param token
     */
    public static void saveToken2Redis(String token){
        if(!StringUtils.isEmpty(token)){
            SysUserDetails sysUserDetails = JWTTokenUtil.parseAccessToken(token);
            token = token.substring(JWTConfig.tokenPrefix.length());
            //存储token
            jwtTokenUtil.redisUtil.hset(token,"username",sysUserDetails.getUsername(),JWTConfig.refreshTime);
            jwtTokenUtil.redisUtil.hset(token,"ip",sysUserDetails.getIp(),JWTConfig.refreshTime);
            jwtTokenUtil.redisUtil.hset(token,"expiration",sysUserDetails.getExpiration(),JWTConfig.refreshTime);
            jwtTokenUtil.redisUtil.hset(token,"refreshTime",sysUserDetails.getRefreshTime(),JWTConfig.refreshTime);
            //映射关系
            jwtTokenUtil.redisUtil.hset("token-mapper",sysUserDetails.getUsername(),token,JWTConfig.refreshTime);
        }
    }

    /**
     * 添加token到黑名单
     * @param token
     */
    public static void addBlackList(String token){
        if(!StringUtils.isEmpty(token)){
            String username = JWTTokenUtil.getUserNameByToken(token);
            token = token.substring(JWTConfig.tokenPrefix.length());
            jwtTokenUtil.redisUtil.hdel("token-mapper",username);
            jwtTokenUtil.redisUtil.hset("black-list",token,dateTimeFormatter.format(LocalDateTime.now()));
            jwtTokenUtil.redisUtil.del(token);
        }
    }

    /**
     * 从黑名单删除token
     * @param token
     */
    public static void deleteTokenFromBlackList(String token){
        if(!StringUtils.isEmpty(token)){
            token = token.substring(JWTConfig.tokenPrefix.length());
            jwtTokenUtil.redisUtil.hdel("black-list",token);
        }
    }
    /**
     * 从redis删除token
     * @param token
     */
    public static void deleteTokenFromRedis(String token){
        if(!StringUtils.isEmpty(token)){
            token = token.substring(JWTConfig.tokenPrefix.length());
            jwtTokenUtil.redisUtil.del(token);
        }
    }

    /**
     * 判断是不是黑名单中的token
     * @param token
     * @return
     */
    public static boolean isInBlackList(String token){
        boolean result = false;
        if(!StringUtils.isEmpty(token)){
            token = token.substring(JWTConfig.tokenPrefix.length());
            result = jwtTokenUtil.redisUtil.hHasKey("black-list",token);
        }
        return result;
    }
    /**
     * 判断redis中token是不是过期了
     * @param token true:过期了 false:未过期
     * @return
     */
    public static boolean isExpiration(String token){
        boolean result = false;
        if(!StringUtils.isEmpty(token)){
            String expiration = (String) jwtTokenUtil.redisUtil.hget(token.substring(JWTConfig.tokenPrefix.length()), "expiration");
            LocalDateTime ex = LocalDateTime.parse(expiration, dateTimeFormatter);
            result = LocalDateTime.now().compareTo(ex)>0;
        }
        return result;
    }

    /**
     * 判断token是不是还能刷新
     * @param token  true:还能刷新 false:不能刷新
     * @return
     */
    public static boolean isCanRefresh(String token){
        boolean result = false;
        if(!StringUtils.isEmpty(token)){
            String refreshTime = (String) jwtTokenUtil.redisUtil.hget(token.substring(JWTConfig.tokenPrefix.length()), "refreshTime");
            LocalDateTime ex = LocalDateTime.parse(refreshTime, dateTimeFormatter);
            result = LocalDateTime.now().compareTo(ex)<0;
        }
        return result;
    }

    /**
     * 判断redis有没有token
     * @param token
     * @return
     */
    public static boolean hasToken(String token) {
        if (!StringUtils.isEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            return jwtTokenUtil.redisUtil.hasKey(token);
        }
        return false;
    }
    /**
     * 从redis存在的token解析出用户名
     * @param token token字符串
     * @return 用户名
     */
    public static String getUserNameByToken(String token) {
        String username = null;
        if(!StringUtils.isEmpty(token)){
            username = (String) jwtTokenUtil.redisUtil.hget(token.substring(JWTConfig.tokenPrefix.length()), "username");
        }
        return username;
    }

    /**
     * 从redis存在的token中解析的登录ip
     * @param token
     * @return ip地址
     */
    public static String getIpByToken(String token) {
        String ipAddress = null;
        if(!StringUtils.isEmpty(token)){
            ipAddress = (String) jwtTokenUtil.redisUtil.hget(token.substring(JWTConfig.tokenPrefix.length()), "ip");
        }
        return ipAddress;
    }
}
