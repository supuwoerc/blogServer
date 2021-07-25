package cn.lookup.sanye.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/25 21:29
 * @Desc: 项目配置信息（在filter里面读取不到配置，写了个静态配置来获取配置）
 */
@Data
@Component
@SuppressWarnings("static-access")
public class ProjectInfoBean {
    public static String projectName;
    @Value("${server.servlet.context-path}")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
