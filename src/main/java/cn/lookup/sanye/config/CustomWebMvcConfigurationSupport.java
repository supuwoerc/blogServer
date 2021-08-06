package cn.lookup.sanye.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/8/6 11:29
 * @Desc: 图片映射资源配置  坑：https://www.cnblogs.com/hellohero55/p/12072465.html
 **/
@Configuration
public class CustomWebMvcConfigurationSupport implements WebMvcConfigurer {
    @Value("${upload.default-dir}")
    private String baseDir;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload-images/**").addResourceLocations("file:"+baseDir+ File.separator);
    }
}
