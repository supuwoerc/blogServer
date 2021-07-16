package cn.lookup.sanye.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 14:26
 * @Desc:springSecurity核心配置类
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启注解权限
public class SysSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;   //403权限不足处理类
    @Autowired()
    @Qualifier("UserNotLoginHandler")
    private UserNotLoginHandler userNotLoginHandler;            //401未登录处理类
    @Autowired
    private UserLoginSuccessHandler userLoginSuccessHandler;   //登陆成功处理类
    @Autowired
    private UserLoginFailureHandler userLoginFailureHandler;   //登录失败处理类
    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;  //注销成功处理类
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider; //登录验证处理
    @Autowired
    private UserPermissionEvaluator userPermissionEvaluator;  //用户权限注解
    /**
     * 密码编码
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoderBean(){
        return new BCryptPasswordEncoder();
    }
    /**
     * 注入自定义的PermissionEvaluator
     * @return
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(userPermissionEvaluator);
        return handler;
    }
    /**
     * 注入自定义的用户登录验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userAuthenticationProvider);
    }
    /**
     * 安全权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // 权限配置
                .antMatchers(JWTConfig.antMatchers.split(",")).permitAll()// 获取白名单（不进行权限验证）
                .anyRequest().authenticated() // 其他的需要登陆后才能访问
                .and().httpBasic()
                .and().formLogin().loginProcessingUrl("/login/submit")// 配置登录URL
                .successHandler(userLoginSuccessHandler) // 配置登录成功处理类
                .failureHandler(userLoginFailureHandler) // 配置登录失败处理类
                .and().logout().logoutUrl("/logout/submit")// 配置登出地址
                .logoutSuccessHandler(userLogoutSuccessHandler) // 配置用户登出处理类
                .and().exceptionHandling().accessDeniedHandler(userAccessDeniedHandler)// 配置没有权限处理类
                .authenticationEntryPoint(userNotLoginHandler) // 配置未登录处理类
                .and().cors()// 开启跨域
                .and().csrf().disable(); // 禁用跨站请求伪造防护
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 禁用session（使用Token认证）
        http.headers().cacheControl(); // 禁用缓存
        http.addFilter(new JWTAuthenticationFilter(authenticationManager())); // 添加JWT过滤器
    }
}
