package com.steelman.iot.platform.config;

import com.steelman.iot.platform.utils.IdGeneratorWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tang
 * date 2020-11-18
 */
@Configuration
public class IdWorkerConfig {
    @Bean
    public IdGeneratorWorker idGeneratorWorker(){
        return new IdGeneratorWorker(0);
    }
}
