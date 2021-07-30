package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.vo.Result;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:02
 * @Desc: desc 登陆失败处理类
 */
@Component
public class UserLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //锁定账户的情况下
        if(e instanceof LockedException){
            Result.write(555,e.getMessage(),null,httpServletResponse);
        }
        //其他情况
        Result.write(500,e.getMessage(),null,httpServletResponse);
    }
}
