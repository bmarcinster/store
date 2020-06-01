package com.talent.alpha.store.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talent.alpha.store.repostitory.SlideResultsDynamicRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SlideResultsDynamicRetriever slideResultsDynamicRetriever() {
        return new SlideResultsDynamicRetriever();
    }

}
