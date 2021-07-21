package cn.lookup.sanye.config.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/21 14:15
 * @Desc:登录前校验验证码的过滤器（单独做一个前置过滤器一是为了解耦，二是因为重写了UsernamePasswordAuthenticationFilter自定义了接收json登录
 * 为了保持formData也能登录，所以需要将验证放在登录授权之前做）
 **/
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

    }
}
