package io.github.poojithairosha.query_guard_spring_boot_starter.config;

import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.github.poojithairosha.query_guard_spring_boot_starter.listener.LoggingQueryListener;
import io.github.poojithairosha.query_guard_spring_boot_starter.listener.TrackingQueryListener;
import io.github.poojithairosha.query_guard_spring_boot_starter.logging.QueryGuardLogger;
import io.github.poojithairosha.query_guard_spring_boot_starter.proxy.QueryGuardDataSource;
import io.github.poojithairosha.query_guard_spring_boot_starter.report.QueryGuardReportBuilder;
import io.github.poojithairosha.query_guard_spring_boot_starter.service.QueryGuardExecutor;
import io.github.poojithairosha.query_guard_spring_boot_starter.storage.TraceStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Slf4j
public class QueryGuardConfiguration {

    @Bean
    @ConditionalOnProperty(
            name = "queryguard.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public static BeanPostProcessor queryGuardDataSourcePostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof DataSource dataSource) || bean instanceof QueryGuardDataSource) {
                    return bean;
                }

                log.info("QueryGuard DataSource Initialized for bean '{}'", beanName);
                return new QueryGuardDataSource(
                        dataSource,
                        List.of(
                                new LoggingQueryListener(),
                                new TrackingQueryListener()
                        )
                );
            }
        };
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
            QueryGuardProperties properties,
            TraceStorage traceStorage
    ) {
        return new QueryGuardExecutor(analysisEngine, reportBuilder, logger, properties, traceStorage);
    }

}
