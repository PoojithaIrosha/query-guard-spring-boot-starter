package io.poojithairosha.query_guard_spring_boot_starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
@Import({
        QueryGuardConfig.class,
        QueryGuardWebConfig.class,
        QueryGuardAnalysisConfiguration.class
})
public class QueryGuardAutoConfiguration {
}
