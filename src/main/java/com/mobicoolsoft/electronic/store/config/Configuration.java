package com.mobicoolsoft.electronic.store.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

/**
 * @author Sandip Kolhekar
 * @implNote Configuration class for different Type beans
 */

@org.springframework.context.annotation.Configuration
public class Configuration {

    /**
     * @implNote ModelMapper bean configuration
     * @return ModelMapper class bean
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
