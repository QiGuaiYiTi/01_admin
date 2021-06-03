package com.charles;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.charles.dao")
public class Charles01AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(Charles01AdminApplication.class, args);
    }

}
