package cn.lookup.sanye.utils;

import cn.lookup.sanye.config.security.JWTConfig;
import cn.lookup.sanye.pojo.SysUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 21:16
 * @Desc: JwtToken工具类
 */
@Data
@Slf4j
public class JWTTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUTHORITIES = "authorities";

    /**
     * 生成jwt-token的方法
     *
     * @param sysUserDetails 系统用户实例
     * @return token
     */
    public static String createAccessToken(SysUserDetails sysUserDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, sysUserDetails.getUsername());  //这里的claims信息可以进行扩展
        claims.put(CLAIM_KEY_AUTHORITIES, sysUserDetails.getAuthorities());
        String token = Jwts.builder().setClaims(claims)  // 主体信息
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
                sysUserDetails.setUsername(claims.getSubject());
                //从jwt中解析出角色
                ArrayList<LinkedHashMap<String,String>> authoritiesFromToken = (ArrayList<LinkedHashMap<String, String>>) claims.get(CLAIM_KEY_AUTHORITIES);
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                for (LinkedHashMap<String, String> roleLinkedHashMap : authoritiesFromToken) {
                    String roleName = roleLinkedHashMap.get("authority");
                    authorities.add(new SimpleGrantedAuthority(roleName));
                }
                sysUserDetails.setAuthorities(authorities);
            } catch (Exception e) {
                log.error("jwt解析失败",e.getMessage());
            }
        }
        return sysUserDetails;
    }
}
