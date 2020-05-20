package com.smith.edu.patienttrackerrestservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Config class for exposing all endpoints to any front-end application.
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter
{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}