package com.jishop.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@EnableJpaRepositories(
        basePackages = "com.jishop.log", //적용할 repository 경로
        entityManagerFactoryRef = "logEntityManager", //아래 메서드 명과 일치해야 한다
        transactionManagerRef = "logTransactionManager" //아래 메서드 명과 일치해야한다
 )

@Configuration
public class LogDBConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean logEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(logDataSource());
        em.setPackagesToScan("com.jishop.log");

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        em.setJpaProperties(properties);

        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "logDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.logdb") // 소문자 logdb로 수정
    protected DataSource logDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager logTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(logEntityManager().getObject());

        return transactionManager;
    }

    @Bean
    public JdbcTemplate logJdbcTemplate(@Qualifier("logDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
