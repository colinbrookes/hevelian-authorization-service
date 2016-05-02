package com.hevelian.identity.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;

import com.hevelian.identity.server.config.data.DatabaseConfig;
import com.hevelian.identity.server.config.security.SecurityConfig;

@Configuration
@ComponentScan(basePackages = "com.hevelian.identity", excludeFilters = @Filter(classes = {
        Controller.class, Configuration.class }) )
@Import({ DatabaseConfig.class, SecurityConfig.class })
public class RootConfig {
}
