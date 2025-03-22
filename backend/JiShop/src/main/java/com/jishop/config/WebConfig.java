package com.jishop.config;

import com.jishop.member.annotation.CurrentUserResolver;
import com.jishop.member.annotation.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final CurrentUserResolver currentUserResolver;

    @Autowired
    public WebConfig(LoginInterceptor loginInterceptor, CurrentUserResolver currentUserResolver) {
        this.loginInterceptor = loginInterceptor;
        this.currentUserResolver = currentUserResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/notices")
        .excludePathPatterns("/signin", "/signup", "/wishlist/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserResolver);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }



}
