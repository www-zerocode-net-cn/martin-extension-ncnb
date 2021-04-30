package com.java2e.martin.extension.ncnb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion NcnbApplication
 * @since 1.0
 */
@SpringBootApplication
@MapperScan("com.java2e.martin.extension.ncnb.mapper")
public class NcnbApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcnbApplication.class, args);
    }
}
