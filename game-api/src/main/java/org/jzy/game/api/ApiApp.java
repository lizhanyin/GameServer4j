package org.jzy.game.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.mongodb.autoconfigure.DataMongoAutoConfiguration;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 处理登录，支付等接口,无状态服务器
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataMongoAutoConfiguration.class})
@ComponentScan("org.jzy.game")
public class ApiApp implements CommandLineRunner {
    public static void main(String[] args) {

        SpringApplication.run(ApiApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
