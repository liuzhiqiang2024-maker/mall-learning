package com.example.malldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.malldemo.mapper")
public class MallDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(MallDemoApplication.class, args);
    }

}
