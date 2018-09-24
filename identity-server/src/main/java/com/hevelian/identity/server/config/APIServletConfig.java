package com.hevelian.identity.server.config;

import com.hevelian.identity.server.parameters.RequestParameterNameDiscoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.hevelian.identity",
    includeFilters = @Filter(value = Controller.class), useDefaultFilters = false)
public class APIServletConfig {

  // This adds support of a @Validated annotation.
  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor(LocalValidatorFactoryBean validator) {
    MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
    processor.setValidator(validator);
    return processor;
  }

  /*
We use @Validated annotation in controllers to validate primitive request parameters.
Setting a custom parameters name discoverer lets retrieving API parameters name from
@RequestParam annotation if present. This validator will work with other layers as well,
because if @RequestParam annotation is not used - DefaultParameterNameDiscoverer is called,
which gets parameters names using reflection. But we are not planning to use @Validated
outside Spring Controllers.
 */
  @Bean
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setParameterNameDiscoverer(new RequestParameterNameDiscoverer());
    return validator;
  }
}
