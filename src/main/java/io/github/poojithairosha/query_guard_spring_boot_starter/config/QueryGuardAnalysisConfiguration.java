package io.github.poojithairosha.query_guard_spring_boot_starter.config;

import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.DuplicateQueryAnalyzer;
import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.NPlusOneAnalyzer;
import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalyzer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class QueryGuardAnalysisConfiguration {

    @Bean
    public QueryAnalysisEngine queryAnalysisEngine(List<QueryAnalyzer> analyzers) {
        return new QueryAnalysisEngine(analyzers);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = "queryguard.analyzers.duplicate",
            havingValue = "true",
            matchIfMissing = true
    )
    public DuplicateQueryAnalyzer duplicateQueryAnalyzer() {
        return new DuplicateQueryAnalyzer();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            name = "queryguard.analyzers.n-plus-one",
            havingValue = "true",
            matchIfMissing = true
    )
    public NPlusOneAnalyzer nPlusOneAnalyzer() {
        return new NPlusOneAnalyzer();
    }

}
