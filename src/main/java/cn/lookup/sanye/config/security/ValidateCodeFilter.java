package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.dto.LoginAndRegisterUserDto;
import cn.lookup.sanye.utils.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/21 14:15
 * @Desc:登录前校验验证码的过滤器（单独做一个前置过滤器一是为了解耦，二是因为重写了UsernamePasswordAuthenticationFilter自定义了接收json登录 为了保持formData也能登录，所以需要将验证放在登录授权之前做）
 **/
@Component
public class ValidateCodeFilter extends GenericFilterBean {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserLoginFailureHandler userLoginFailureHandler;
    @Value("${server.servlet.context-path}")
    private String projectName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("校验验证码");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if ((projectName + "/login/submit").equalsIgnoreCase(httpServletRequest.getRequestURI())
                && "post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            try(ServletInputStream is = httpServletRequest.getInputStream()) {
                ObjectMapper objectMapper = new ObjectMapper();
                LoginAndRegisterUserDto dto = objectMapper.readValue(is, LoginAndRegisterUserDto.class);
                validateCode(dto.getCode(), dto.getCodeKey());
            } catch (BadCredentialsException e) {
                userLoginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private void validateCode(String code, String codeKey) throws BadCredentialsException {
        if (redisUtil.hasKey(codeKey)) {
            String realCode = (String) redisUtil.get(codeKey);
            if (StringUtils.isEmpty(realCode) || !realCode.equalsIgnoreCase(code)) {
                throw new BadCredentialsException("验证码错误");
            }
        } else {
            throw new BadCredentialsException("验证码错误");
        }
    }
}
