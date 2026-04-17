package io.github.poojithairosha.query_guard_spring_boot_starter.config;

import io.github.poojithairosha.query_guard_spring_boot_starter.logging.QueryGuardLogger;
import io.github.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReportBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class QueryGuardLoggingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public QueryGuardLogger queryGuardLogger(
            ApplicationEventPublisher applicationEventPublisher,
            JsonMapper jsonMapper
            ) {
        return new QueryGuardLogger(applicationEventPublisher, jsonMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public QueryGuardReportBuilder queryGuardReportBuilder() {
        return new QueryGuardReportBuilder();
    }
}
