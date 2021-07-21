package cn.lookup.sanye;

import cn.lookup.sanye.mapper.UserMapper;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.IUserService;
import cn.lookup.sanye.service.SysUserService;
import cn.lookup.sanye.service.impl.SysUserDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SanyeApplicationTests {
    @Autowired
    SysUserService sysUserService;
    @Test
    void contextLoads() {
        System.out.println("springboot");
    }
    @Test
    void sysUserDetailsTest(){
        List<Role> rolesByUserId = sysUserService.findRolesByUserId(3l);
        System.out.println(rolesByUserId);
    }
}
