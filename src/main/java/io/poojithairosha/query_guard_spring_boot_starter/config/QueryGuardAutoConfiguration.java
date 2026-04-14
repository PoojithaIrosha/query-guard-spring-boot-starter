package io.poojithairosha.query_guard_spring_boot_starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
@EnableConfigurationProperties(QueryGuardProperties.class)
@Import({
        QueryGuardConfiguration.class,
        QueryGuardWebConfiguration.class,
        QueryGuardAnalysisConfiguration.class,
        QueryGuardLoggingConfiguration.class,
        QueryGuardMetricsConfiguration.class
})
public class QueryGuardAutoConfiguration {
}
