package cn.lookup.sanye.config;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.utils.JWTTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 22:52
 * @Desc: 用户登录成功的处理类
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
        String accessToken = JWTTokenUtil.createAccessToken(sysUserDetails);
        HashMap<String, Object> result = new HashMap<>();
        result.put("token",accessToken);
        Result.write(200,"登陆成功",result,httpServletResponse);
    }
}
