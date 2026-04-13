package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.poojithairosha.query_guard_spring_boot_starter.listener.LoggingQueryListener;
import io.poojithairosha.query_guard_spring_boot_starter.listener.TrackingQueryListener;
import io.poojithairosha.query_guard_spring_boot_starter.logging.QueryGuardLogger;
import io.poojithairosha.query_guard_spring_boot_starter.proxy.QueryGuardDataSource;
import io.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReportBuilder;
import io.poojithairosha.query_guard_spring_boot_starter.service.QueryGuardExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Slf4j
public class QueryGuardConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public DataSource queryGuardDataSource(ObjectProvider<DataSource> dataSourceProvider) {
        DataSource originalDataSource = dataSourceProvider.getIfAvailable();
        if (originalDataSource == null) {
            return null;
        }

        log.info("QueryGuard DataSource Initialized");
        return new QueryGuardDataSource(
                originalDataSource,
                List.of(
                        new LoggingQueryListener(),
                        new TrackingQueryListener()
                )
        );
    }


    @Bean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public QueryGuardExecutor queryGuardExecutor(
            QueryAnalysisEngine analysisEngine,
            QueryGuardReportBuilder reportBuilder,
            QueryGuardLogger logger,
            QueryGuardProperties properties
    ) {
        return new QueryGuardExecutor(analysisEngine, reportBuilder, logger, properties);
    }

}

