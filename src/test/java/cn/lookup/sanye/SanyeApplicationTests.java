package cn.lookup.sanye;

import cn.lookup.sanye.config.ProjectInfoBean;
import cn.lookup.sanye.mapper.SysUserMapper;
import cn.lookup.sanye.mapper.UserMapper;
import cn.lookup.sanye.service.MailService;
import cn.lookup.sanye.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SanyeApplicationTests {
    @Autowired
    private MailService mailService;
    @Autowired
    private SysUserService sysUserService;

    @Test
    void contextLoads() {
        System.out.println("springboot");
    }

    @Test
    void sendMail() {
        Map<String, Object> dataMap = new HashMap<>();
        System.out.println(ProjectInfoBean.getServerUrl());
        dataMap.put("activeLink", ProjectInfoBean.getServerUrl());
        mailService.sendTemplateMail("zhangzhouou@gmail.com", "激活账户", "activeUserTemplate.html", dataMap);
    }

    @Test
    void activeUser() {
        sysUserService.activeUser("zhangqm", "111111");
    }

    @Test
    void getSuffix() {
        String name = "1.";
        System.out.println(name.lastIndexOf("."));
        System.out.println(name.substring(name.lastIndexOf(".") + 1));
    }
}
