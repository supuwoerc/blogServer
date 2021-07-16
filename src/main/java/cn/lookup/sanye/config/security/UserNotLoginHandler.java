package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 22:46
 * @Desc: 401未登录的情况
 */
@Component
public class UserNotLoginHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Result result = Result.fail(401, "未登录", null);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
