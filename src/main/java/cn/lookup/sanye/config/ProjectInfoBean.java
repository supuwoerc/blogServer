package cn.lookup.sanye.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    private static int serverPort;
    @Value("${server.port}")
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    public static String getServerUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://"+address.getHostAddress() +":"+serverPort+projectName;
    }
}
