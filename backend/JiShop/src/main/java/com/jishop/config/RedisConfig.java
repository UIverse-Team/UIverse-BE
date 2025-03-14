package com.jishop.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.SessionRepositoryFilter;

import java.util.Arrays;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper());
    }

    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Java 8 날짜/시간 타입 지원
        mapper.registerModule(new JavaTimeModule());
        // 날짜를 타임스탬프로 출력하지 않음
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Redis 전용 default typing 활성화 (HTTP 요청에는 영향을 주지 않음)
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }

    @Bean
    public FilterRegistrationBean<SessionRepositoryFilter<?>> redisSessionFilterRegistration(SessionRepositoryFilter<?> sessionRepositoryFilter) {
        FilterRegistrationBean<SessionRepositoryFilter<?>> registrationBean = new FilterRegistrationBean<>(sessionRepositoryFilter);
        // /auth/* 패턴에만 Redis 세션 저장소 적용 (즉, 로그인 관련 요청에만)
        registrationBean.setUrlPatterns(Arrays.asList("/auth/*"));
        return registrationBean;
    }
}
