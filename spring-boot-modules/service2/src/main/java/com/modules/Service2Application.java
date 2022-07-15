package com.modules;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yaoyinong
 * @date 2022/7/15 08:55
 * @description
 */
@SpringBootApplication
@MapperScan("com.modules.mapper")
public class Service2Application {

    public static void main(String[] args) {
        SpringApplication.run(Service2Application.class);
    }

}
