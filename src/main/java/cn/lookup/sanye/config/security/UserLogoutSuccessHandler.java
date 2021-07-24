package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.utils.JWTTokenUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String token = httpServletRequest.getHeader(JWTConfig.tokenHeader);
        JWTTokenUtil.addBlackList(token);
        log.info("用户{}注销登录,token已被添加到redis黑名单中",JWTTokenUtil.getUserNameByToken(token));
        SecurityContextHolder.clearContext();
        Result.write(200, "注销成功", null, httpServletResponse);
    }
}
