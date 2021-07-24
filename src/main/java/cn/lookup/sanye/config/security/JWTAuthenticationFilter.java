package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.utils.JWTTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 11:16
 * @Desc:JWTtoken过滤器
 * 1.加入黑名单后将拦截；
 * 2.Token过期但在刷新期间内将刷新Token；
 * 3.超过过期时间且超过刷新时间，将拦截；
 * 4.请求IP与Token中IP不一致，将拦截。
 **/
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JWTConfig.tokenHeader);
        if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
            if(JWTTokenUtil.isInBlackList(token)){
                Result.write(501,"登录已被注销",null,response);
                return;
            }
            //token存在在redis
            if(JWTTokenUtil.hasToken(token)){
                SysUserDetails sysUserDetails = JWTTokenUtil.parseAccessToken(token);
                if (sysUserDetails != null) {
                    //设置上下文对象
                    Collection<? extends GrantedAuthority> authorities = sysUserDetails.getAuthorities();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(sysUserDetails, sysUserDetails.getPassword(), authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("jwt过滤器解析token设置上下文对象...");
                }
            }

        }
        chain.doFilter(request, response);
    }
}
