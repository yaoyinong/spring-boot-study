package com.elasticsearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yaoyinong
 * @date 2022/7/21 21:38
 * @description
 */
@SpringBootApplication
@MapperScan("com.elasticsearch.mapper")
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class);
    }

}
