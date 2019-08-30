package life.lv.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("life.lv.community.mapper")
@EnableScheduling
public class CommunityApplication {

    public static void main(String[] args) {

        SpringApplication.run(CommunityApplication.class, args);
    }

}
