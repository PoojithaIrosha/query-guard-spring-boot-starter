package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.logging.QueryGuardLogger;
import io.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReportBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryGuardLoggingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public QueryGuardLogger queryGuardLogger() {
        return new QueryGuardLogger();
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
