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
     * @return ModelMapper class bean
     * @implNote ModelMapper bean configuration
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();

    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
