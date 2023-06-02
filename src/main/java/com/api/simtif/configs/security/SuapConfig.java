package com.api.simtif.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SuapConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}