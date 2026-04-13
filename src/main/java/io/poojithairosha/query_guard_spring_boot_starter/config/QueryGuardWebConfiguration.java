package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.service.QueryGuardExecutor;
import io.poojithairosha.query_guard_spring_boot_starter.web.QueryGuardFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryGuardWebConfiguration {

    @Bean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public QueryGuardFilter queryGuardFilter(
            QueryGuardExecutor executor
            ) {
        return new QueryGuardFilter(executor);
    }

    @Bean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public FilterRegistrationBean<QueryGuardFilter> queryGuardFilterRegistration(QueryGuardFilter queryGuardFilter) {
        FilterRegistrationBean<QueryGuardFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(queryGuardFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

}
