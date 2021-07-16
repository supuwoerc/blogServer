package cn.lookup.sanye.config.security;

import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.utils.JWTTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 11:16
 * @Desc:JWTtoken过滤器
 **/
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JWTConfig.tokenHeader);
        if(token!=null&&token.startsWith(JWTConfig.tokenPrefix)){
            SysUserDetails sysUserDetails = JWTTokenUtil.parseAccessToken(token);
            if(sysUserDetails!=null){
                //设置上下文对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(sysUserDetails, sysUserDetails.getPassword(), sysUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request,response);
    }
}
