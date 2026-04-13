package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalyzer;
import io.poojithairosha.query_guard_spring_boot_starter.web.QueryGuardFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class QueryGuardWebConfig {

    @Bean
    public FilterRegistrationBean<QueryGuardFilter> queryGuardFilter(QueryAnalysisEngine queryAnalysisEngine) {
        FilterRegistrationBean<QueryGuardFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new QueryGuardFilter(queryAnalysisEngine));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

}
