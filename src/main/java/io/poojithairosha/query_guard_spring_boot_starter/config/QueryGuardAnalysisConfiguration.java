package io.poojithairosha.query_guard_spring_boot_starter.config;

import io.poojithairosha.query_guard_spring_boot_starter.analysis.DuplicateQueryAnalyzer;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.NPlusOneAnalyzer;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalysisEngine;
import io.poojithairosha.query_guard_spring_boot_starter.analysis.QueryAnalyzer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    public DuplicateQueryAnalyzer duplicateQueryAnalyzer() {
        return new DuplicateQueryAnalyzer();
    }

    @Bean
    @ConditionalOnMissingBean
    public NPlusOneAnalyzer nPlusOneAnalyzer() {
        return new NPlusOneAnalyzer();
    }

}
