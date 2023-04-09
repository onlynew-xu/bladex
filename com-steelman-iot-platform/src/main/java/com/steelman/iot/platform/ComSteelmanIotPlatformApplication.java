package com.steelman.iot.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.steelman.iot.platform.dao,com.steelman.iot.platform.energyManager.dao")
public class ComSteelmanIotPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComSteelmanIotPlatformApplication.class, args);
    }

}
