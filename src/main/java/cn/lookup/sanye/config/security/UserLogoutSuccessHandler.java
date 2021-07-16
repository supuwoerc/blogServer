package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.vo.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:03
 * @Desc: 注销成功处理类
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        Result.write(200, "注销成功", null, httpServletResponse);
    }
}
