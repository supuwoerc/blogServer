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