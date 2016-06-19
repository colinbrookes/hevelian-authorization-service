package com.hevelian.identity.server.config.data;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.persistence.config.EntityManagerProperties;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hevelian.identity.server.tenants.jpa.SuperAdminTenantAwareJpaTransactionManager;
import com.hevelian.identity.server.tenants.resolvers.SessionTenantResolver;

@Configuration
@EnableJpaRepositories(basePackages = "com.hevelian.identity")
@EnableTransactionManagement
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.hevelian.identity");
        factory.setDataSource(dataSource());
        // TODO Enable dynamic weaving for performance
        factory.getJpaPropertyMap().put(PersistenceUnitProperties.WEAVING,
                Boolean.FALSE.toString());
//        factory.getJpaPropertyMap().put(PersistenceUnitProperties.LOGGING_LEVEL,
//                SessionLog.FINEST_LABEL);
        factory.getJpaPropertyMap().put(PersistenceUnitProperties.LOGGING_PARAMETERS,
                Boolean.TRUE.toString());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        SuperAdminTenantAwareJpaTransactionManager txManager = new SuperAdminTenantAwareJpaTransactionManager();
        txManager.setTenantResolver(new SessionTenantResolver());
        txManager.setMultitenantProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT);
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
