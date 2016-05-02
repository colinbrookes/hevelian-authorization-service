package com.hevelian.identity.server.config;

import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.hevelian.identity.server.config.rest.RestContextConfig;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected void customizeRegistration(Dynamic registration) {
        // Process 404 as other errors returning well formatted error response.
        registration.setInitParameter("throwExceptionIfNoHandlerFound", Boolean.toString(true));
        super.customizeRegistration(registration);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { APIServletConfig.class, RestContextConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/api/*" };
    }
}