package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.listener.LoggingQueryListener;
import io.poojithairosha.query_guard_spring_boot_starter.listener.TrackingQueryListener;
import io.poojithairosha.query_guard_spring_boot_starter.proxy.QueryGuardDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Slf4j
public class QueryGuardConfig {

    @Bean
    @Primary
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

}

