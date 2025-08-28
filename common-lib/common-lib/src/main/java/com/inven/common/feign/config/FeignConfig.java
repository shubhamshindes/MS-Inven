package com.inven.common.feign.config;

import feign.Feign;

import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        FeignDecorators decorators = FeignDecorators.builder().build();
        return Resilience4jFeign.builder(decorators)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder());
    }
}