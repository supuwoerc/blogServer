package cn.lookup.sanye.config.security;

import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.impl.SysUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 9:53
 * @Desc:用户登录验证处理类
 **/
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SysUserDetailsService sysUserDetailsService;
    //这里懒加载,因为passwordEncoder来自SysSecurityConfig,而SysSecurityConfig又注入了UserAuthenticationProvider,形成循环注入
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    //https://www.jianshu.com/p/693914564406   这里扩展为支持接收json格式的数据，而非仅仅是form-data
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();  //用户名
        String password = (String) authentication.getCredentials();  //密码(明文)
        SysUserDetails sysUserDetails = (SysUserDetails) sysUserDetailsService.loadUserByUsername(username);
        if(!passwordEncoder.matches(password,sysUserDetails.getPassword())){
            throw new BadCredentialsException("用户名或者密码错误");  //此处就是密码错误,但是为了安全所以模糊提示(在loadUserByUsername中就会校验账户存不存在)
        }
        if(sysUserDetails.getStatus().equals(-1)){
            throw new LockedException("账户未激活");
        }
        return new UsernamePasswordAuthenticationToken(sysUserDetails,password,sysUserDetails.getAuthorities());
    }
    /**
     * 支持指定的身份验证
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }


}
