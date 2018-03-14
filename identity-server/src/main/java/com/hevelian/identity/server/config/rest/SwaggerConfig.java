package com.hevelian.identity.server.config.rest;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import com.google.common.collect.Sets;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket api() {
    // Security (TODO refactor after testing)
    AuthorizationScope[] authScopes = new AuthorizationScope[1];
    authScopes[0] = new AuthorizationScopeBuilder().scope("require_auth")
        .description("Scope that requires authorized access").build();
    SecurityReference securityReference =
        SecurityReference.builder().reference("default").scopes(authScopes).build();
    List<SecurityContext> securityContexts = Arrays.asList(
        (SecurityContext.builder().securityReferences(Arrays.asList((securityReference))).build()));

    // Docket
    return new Docket(DocumentationType.SWAGGER_2)
        .directModelSubstitute(LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(OffsetDateTime.class, java.util.Date.class)
        .consumes(
            Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE))
        .produces(
            Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE))
        .securitySchemes(Arrays.asList((new BasicAuth(securityReference.getReference()))))
        .securityContexts(securityContexts).select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any()).build();
  }
}
