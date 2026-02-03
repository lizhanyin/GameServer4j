package org.mmo.bill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 参考
 * https://spring.io/guides/tutorials/spring-boot-kotlin/
 *
 * 启动
 */
@SpringBootApplication
@ComponentScan("org.mmo")
public class BillApp {

    public static void main(String[] args) {
        SpringApplication.run(BillApp.class, args);
    }
}
