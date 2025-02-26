package com.jishop.config;

import com.jishop.property.AuthProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;



@Configuration
@PropertySource(value = "classpath:env.properties", ignoreResourceNotFound = true)
@ConfigurationPropertiesScan(basePackageClasses = AuthProperty.class)
public class PropertyConfig {
    // @PropertySource를 통해 외부 프로퍼티 파일에서 설정값 읽어옴
    // AuthProperty가 스캔됨
}
