package cn.lookup.sanye.config.security;

import cn.lookup.sanye.common.dto.SysUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/17 15:47
 * @Desc: 重写UsernamePasswordAuthenticationFilter来获取提交的参数(spring - security默认是form - data, 修改为先
 *判断参数是否为json,是的话解析json,不是json的话依旧用父类里面的方法)
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * obtainUsername和obtainPassword方法的注释已经说了,可以让子类来自定义用户名和密码的获取工作,
     * 但是我们不打算重写这两个方法,而是重写它们的调用者attemptAuthentication方法,因为json反序列化毕竟有一定消耗,
     * 不会反序列化两次,只需要在重写的attemptAuthentication方法中检查是否json登录,
     * 然后直接反序列化返回`Authentication`对象即可。
     * 这样我们没有破坏原有的获取流程,还是可以重用父类原有的attemptAuthentication方法来处理表单登录
     **/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //Content-Type为json的情况下
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            //用jackson处理参数
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()) {
                SysUserDto sysUserDto = mapper.readValue(is, SysUserDto.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        sysUserDto.getUsername(), sysUserDto.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            } finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}
