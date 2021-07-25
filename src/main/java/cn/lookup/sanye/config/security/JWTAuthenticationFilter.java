package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.config.ProjectInfoBean;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.utils.AccessAddressUtils;
import cn.lookup.sanye.utils.JWTTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 11:16
 * @Desc:JWTtoken过滤器
 * 1.加入黑名单后将拦截；
 * 2.Token过期但在刷新期间内将刷新Token；
 * 3.超过过期时间且超过刷新时间，将拦截；
 **/
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String[] dontNeedFilter = JWTConfig.antMatchers.split(",");
        //https://blog.csdn.net/xgangzai/article/details/118980297
        String path = request.getRequestURI().substring(ProjectInfoBean.projectName.length());
        if(Arrays.asList(dontNeedFilter).contains(path)){
            chain.doFilter(request, response);
            return;
        }
        if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
            //黑名单的token
            if(JWTTokenUtil.isInBlackList(token)){
                Result.write(501,"登录已被注销",null,response);
                return;
            }
            //token存在在redis,但需要校验token的时效性
            if(JWTTokenUtil.hasToken(token)){
                String ipAddress = AccessAddressUtils.getIpAddress(request);
                //判断token是否过期
                if(JWTTokenUtil.isExpiration(token)){
                    //过期但还在刷新期限内
                    if(JWTTokenUtil.isCanRefresh(token)){
                        //根据旧的token获取账户信息后,删除失效token,存储新的token
                        String newToken = JWTTokenUtil.refreshAccessToken(token,ipAddress);
                        JWTTokenUtil.deleteTokenFromRedis(token);
                        JWTTokenUtil.saveToken2Redis(newToken);
                        log.info("用户{}token过期,但依然可刷新,已经更新token");
                        Result.write(502,"收到船新令牌",newToken,response);
                        return;
                    }else{
                        JWTTokenUtil.deleteTokenFromRedis(token);
                        log.info("用户{}token过期,不可刷新,需要重新登录");
                        Result.write(501,"登录令牌已失效",null,response);
                        return;
                    }
                }else{
                    //未过期
                    SysUserDetails sysUserDetails = JWTTokenUtil.parseAccessToken(token);
                    if (sysUserDetails != null) {
                        //设置上下文对象
                        setSecurityContextBySysUserDetails(sysUserDetails);
                    }
                }
            }

        }
        chain.doFilter(request, response);
    }

    /**
     * 将授权用户设置到上下文中
     * @param sysUserDetails
     */
    private void setSecurityContextBySysUserDetails(SysUserDetails sysUserDetails){
        if (sysUserDetails != null) {
            //设置上下文对象
            Collection<? extends GrantedAuthority> authorities = sysUserDetails.getAuthorities();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(sysUserDetails, sysUserDetails.getPassword(), authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("jwt过滤器解析token设置上下文对象...");
        }
    }
}
