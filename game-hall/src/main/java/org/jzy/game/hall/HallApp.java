package org.jzy.game.hall;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.mongodb.autoconfigure.DataMongoAutoConfiguration;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 游戏服启动类
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataMongoAutoConfiguration.class})
@ComponentScan("org.jzy.game")
@EnableMongoRepositories(basePackages = "org.jzy.game.hall.db.repository")
public class HallApp implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HallApp.class, args);
	}

	public void run(String... args) throws Exception {
	}

}
