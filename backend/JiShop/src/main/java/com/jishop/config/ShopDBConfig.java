package com.jishop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.sql.DataSource;
import java.util.Properties;

@EnableJpaRepositories(
        basePackages = "com.jishop",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.jishop\\.log.*"),
        entityManagerFactoryRef = "shopEntityManager",
        transactionManagerRef = "shopTransactionManager"
)
@Configuration
public class ShopDBConfig {
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean shopEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(shopDataSource());
        em.setPackagesToScan("com.jishop");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        properties.setProperty("hibernate.implicit_naming_strategy",
                "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean(name = "shopDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.shopdb")
    protected DataSource shopDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager shopTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(shopEntityManager().getObject());

        return transactionManager;
    }

    @Bean
    @Primary
    public JdbcTemplate shopJdbcTemplate(@Qualifier("shopDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
