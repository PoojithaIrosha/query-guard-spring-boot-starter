package io.github.poojithairosha.query_guard_spring_boot_starter.config;

import io.github.poojithairosha.query_guard_spring_boot_starter.listener.QueryGuardMetricsListener;
import io.github.poojithairosha.query_guard_spring_boot_starter.metrics.QueryGuardMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MeterRegistry.class)
public class QueryGuardMetricsConfiguration {

    @Bean
    public QueryGuardMetrics queryGuardMetrics(ObjectProvider<MeterRegistry> registryProvider) {
        MeterRegistry registry = registryProvider.getIfAvailable();
        if (registry == null) {
            return null;
        }
        return new QueryGuardMetrics(registry);
    }

    @Bean
    @ConditionalOnBean(QueryGuardMetrics.class)
    public QueryGuardMetricsListener queryGuardMetricsListener(QueryGuardMetrics queryGuardMetrics) {
        return new QueryGuardMetricsListener(queryGuardMetrics);
    }

}
