package cn.lookup.sanye;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("cn.lookup.sanye.mapper")
public class SanyeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SanyeApplication.class, args);
    }
}
