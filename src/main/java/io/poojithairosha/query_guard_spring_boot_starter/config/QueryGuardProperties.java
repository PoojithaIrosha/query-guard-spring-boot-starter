package io.poojithairosha.query_guard_spring_boot_starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "queryguard")
@Data
public class QueryGuardProperties {

    private boolean enabled = true;

    private int maxQueriesPerRequest = 50;

    private Analyzers analyzers = new Analyzers();

    private Logging logging = new Logging();

    private Tracing tracing = new Tracing();


    @Data
    public static class Analyzers {
        private boolean duplicate = true;
        private boolean nPlusOne = true;
    }

    @Data
    public static class Logging {
        private boolean enabled = true;
        private String level = "WARNING";
    }

    @Data
    public static class Tracing {
        private List<String> excludePaths;
    }
}
