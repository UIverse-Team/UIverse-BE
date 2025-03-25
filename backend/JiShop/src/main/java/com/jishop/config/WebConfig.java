package com.jishop.config;

import com.jishop.member.annotation.CurrentUserResolver;
import com.jishop.member.annotation.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${swagger.url}")
    private String swaggerUrl;

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
        .excludePathPatterns("/auth/signin", "/signup", "/wishlist/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

        // todo: 프론트주소 넘겨주고 있는데 백엔드 주소 넘겨줘야하나? (3/23)
        corsRegistration.allowedOrigins("https://uiverse.shop", "http://localhost:3000", swaggerUrl);
    }

    // todo: 추후 반영 결정해야할 사항 (3/23)
    /* @Bean
    public FilterRegistrationBean<SecurityHeadersFilter> securityHeadersFilter() {
        FilterRegistrationBean<SecurityHeadersFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SecurityHeadersFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }*/

    // todo: 추후 반영 결정해야할 사항 (3/23)
    /*@Bean
    public FilterRegistrationBean<CsrfFilter> csrfFilter() {
        FilterRegistrationBean<CsrfFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CsrfFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }*/
}
