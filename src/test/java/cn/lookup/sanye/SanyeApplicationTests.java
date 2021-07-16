package cn.lookup.sanye;

import cn.lookup.sanye.mapper.UserMapper;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SanyeApplicationTests {
    @Test
    void contextLoads() {
        System.out.println("springboot");
    }
}
