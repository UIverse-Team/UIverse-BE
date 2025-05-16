package com.jiseller.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@EnableJpaRepositories(
        basePackages = "com.jiseller",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.jiseller\\.log.*"),
        entityManagerFactoryRef = "sellerEntityManager",
        transactionManagerRef = "sellerTransactionManager"
)
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ShopDBConfig {
    private final Environment env;
    @Bean(name = "sellerEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean sellerEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(shopDataSource());
        em.setPackagesToScan("com.jiseller");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();

        properties.setProperty("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto", "update"));

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        properties.setProperty("hibernate.implicit_naming_strategy",
                "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl");
        em.setJpaProperties(properties);

        return em;
    }

    @Primary
    @Bean(name = "shopDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shopdb")
    protected DataSource shopDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager sellerTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(sellerEntityManager().getObject());

        return transactionManager;
    }

    @Bean
    @Primary
    public JdbcTemplate shopJdbcTemplate(@Qualifier("shopDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
