package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.web.QueryGuardFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryGuardWebConfig {

    @Bean
    public FilterRegistrationBean<QueryGuardFilter> queryGuardFilter() {
        FilterRegistrationBean<QueryGuardFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new QueryGuardFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

}
