package com.hevelian.identity.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.hevelian.identity",
    includeFilters = @Filter(value = Controller.class), useDefaultFilters = false)
public class APIServletConfig {
}
