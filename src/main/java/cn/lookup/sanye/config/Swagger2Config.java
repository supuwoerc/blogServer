package cn.lookup.sanye.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 14:41
 * @Desc:Swagger2的配置类
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Value("${swagger.enable}")
    private boolean swaggerEnable;   //是否开启文档(区分开发环境和生产环境来开启关闭接口文档)
    /**
     * 定义扫描那些包下面的文件生成api文档
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.lookup.sanye.controller"))
                .paths(PathSelectors.any())
                .build()
                //设置需要认证的路径
                .securityContexts(securityContexts())
                //自定义请求头的参数
                .securitySchemes(securitySchemes());
    }
    /**
     * 定义展示的信息，例如标题、描述、版本等
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("sanye").description("接口文档")
                .termsOfServiceUrl("http://127.0.0.1:8080/").version("1.0")
                .build();
    }
}
