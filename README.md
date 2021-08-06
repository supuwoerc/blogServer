### 涉及技术或框架
* springboot作为服务基础框架
* mybatis-plus作为持久层框架，此外利用mybatis-plus的逆向工程生成工程的部分代码
* redis作为缓存和分布式锁
* spring-security作为安全框架，为系统用户认证授权，分发jwt令牌
* mysql作为数据库
* swagger2实现文档开发记录(此外集成了第三方的UI库)
* logback作为日志记录框架(springboot自带)
* mail实现邮件发送
* easy-captcha实现验证码

### TODO
* 实现OAuth2.0实现常用微信，微博，QQ单点登录
* 评论
* RabbitMQ集成
* Websocket集成

### 遇到的问题和解决
* IDEA中运行正常，打包后报错(BeanCurrentlyInCreationException...Requested bean is currently in creation: Is there an unresolvable circular reference...)
    - 循环注入导致的报错，排查了一下是因为UserAuthenticationProvider注入的PasswordEncoder来自SysSecurityConfig中的Bean，而SysSecurityConfig又注入了UserAuthenticationProvider，形成循环注入
    - 解决:@Lazy注入 
    - 参考:https://blog.csdn.net/revivedsun/article/details/84642316
* SpringSecurity改造让登录能接受json登录(因为过滤器的原因最终还是采用表单登录)
    - 因为SpringSecurity登录中的参数底层是使用`request.getParameter()`来获取的，可以重写`UsernamePasswordAnthenticationFilter`中的`obtainUsername`和`obtainPassword`方法，让子类来自定义用户名和密码的获取工作，但是我们不打算重写这两个方法，而是重写它们的调用者`attemptAuthentication`方法，因为json反序列化毕竟有一定消耗，不会反序列化两次，只需要在重写的`attemptAuthentication`方法中检查是否json登录，然后直接反序列化返回`Authentication`对象即可。这样我们没有破坏原有的获取流程，还是可以重用父类原有的`attemptAuthentication`方法来处理表单登录。
    - 重写后支持两种方式登录，但后续添加登录验证码过滤器的时候发生问题，一旦在filter中读取json，后续的controller就没法再使用流来获取参数了，原因：servlet规范里提到过，在servlet中流只能读取一次。也就是说，在设计规范中这两种方式是冲突的。
    - 尝试让流可以重复使用，自己继承HttpServletRequestWrapper 包装request来备份流，实际上是可行的，但是为了这一点点扩展，大动干戈，并且可能会导致其他一些问题，最终改造了前端的post请求，在登录的接口传递`Content-type`为`application/x-www-form-urlencoded`，其余还是json。

* SpringSecurity自定义实现权限不足处理类`AccessDeniedHandler`的失效问题，原因是因为自己还定义了`GlobalDefaultExceptionHandler`来捕获全局的异常，但是这个自定义的全局捕获的处理类先于捕获到异常，直接返回json结果。
  * 原有的 `GlobalDefaultExceptionHandler`不用修改，只需要增加一个自定义的 `AccessDeniedExceptionHandler `提前捕获403异常即可。
  * 可以实现`Ordered`接口，或使用注解`@Order`来解决，一定要注意异常的包名，刚刚一直没成功，排查半天竟然是导错包了！~
  * https://github.com/spring-projects/spring-security/issues/6908
  * https://stackoverflow.com/questions/31074040/custom-accessdeniedhandler-not-called
* RBAC的权限设计的疑惑：原本设计RBAC的五张表，想着将权限表当做菜单表实现动态菜单，也就是权限是菜单的体现，角色是权限的集合，人是角色的集合，但是实际上是不适合的，因为在token未过期的情况下，后台想要限制某一个角色的权限，只能依靠用户重新登录来刷新菜单来实现，这点是不恰当的，所以最优解是降低访问的权限粒度，将权限和菜单分开，即使用户存在这个菜单，因为不知道菜单是何时请求的产物，也需要验证权限。
* 图片上传到服务器上的预览问题
  * 搭建Nginx图片服务器
  * Springboot搭建图片服务器中继承`WebMvcConfigurationSupport`重写`addResourceHandlers`导致其他静态资源，例如Swagger2的页面访问不了，static路径也访问不到，原因其实就是当我们使用WebMvcConfigurationSupport时WebMvc自动化配置就会失效
    * 解决法1：最简单的解决办法就是将：`extends WebMvcConfigurationSupport`替换为`implements WebMvcConfigure`
    * 解决法2：
    ```
    @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            //重写这个方法，映射静态资源文件
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/resources/")
                    .addResourceLocations("classpath:/static/")
                    .addResourceLocations("classpath:/public/");
            super.addResourceHandlers(registry);
        }
    ```
  