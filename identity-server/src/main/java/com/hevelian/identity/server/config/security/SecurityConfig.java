package com.hevelian.identity.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfFilter;

import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ROOT_ADMIN");
        auth.userDetailsService(userDetailsService);
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Check whether it is possible to set login URL to Spring directly.
        final String loginURL = "/api/LoginService/login";
        http.authorizeRequests()
                .antMatchers(loginURL, "/api/v2/api-docs", "/api/swagger-ui.html",
                        "/api/webjars/**", "/api/swagger-resources", "/api/configuration/ui",
                        "/api/configuration/security")
                .permitAll().anyRequest().authenticated().and().httpBasic();

        // Disable scrf as there is not a high chance somebody will call
        // endpoint through the browser directly.
        http.csrf().disable();
        // TODO possibly turn on csrf per configuration
        // turnOnCsrf(http, loginURL);
    }

    // Not currently used, but tested.
    @SuppressWarnings("unused")
    private void turnOnCsrf(HttpSecurity http, final String loginURL) throws Exception {
        // http://www.codesandnotes.be/2015/02/05/spring-securitys-csrf-protection-for-rest-services-the-client-side-and-the-server-side/
        http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class);
        // Allow post to login page without csrf tokens
        http.csrf().ignoringAntMatchers(loginURL);
    }
}