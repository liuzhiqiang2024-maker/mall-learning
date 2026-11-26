package com.example.malldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 从当前包扫描 Controller、Service 等组件，并启动 Web 应用。
@SpringBootApplication
// 扫描 Mapper 接口，由 MyBatis 提供可以执行 SQL 的实现。
@MapperScan("com.example.malldemo.mapper")
public class MallDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(MallDemoApplication.class, args);
    }

}
