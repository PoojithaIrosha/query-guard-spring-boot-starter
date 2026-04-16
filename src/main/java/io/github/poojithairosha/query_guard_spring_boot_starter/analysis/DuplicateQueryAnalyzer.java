package io.github.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.github.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.github.poojithairosha.query_guard_spring_boot_starter.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DuplicateQueryAnalyzer implements QueryAnalyzer {

    private static final int DUPLICATE_THRESHOLD = 1;
    private static final String ANALYZER_TYPE = "DUPLICATE_QUERY";

    @Override
    public List<AnalysisResult> analyze(QueryContext context) {
        log.debug("[DuplicateQueryAnalyzer] start analyzing");

        if (context == null || context.getQueries().isEmpty()) {
            log.debug("[DuplicateQueryAnalyzer] nothing to analyze. skipping");
            return Collections.emptyList();
        }

        Map<String, Long> queryCountMap = context.getQueries()
                .stream()
                .collect(Collectors.groupingBy(
                        SqlUtil::normalizeSQL,
                        Collectors.counting()
                ));

        log.debug("[DuplicateQueryAnalyzer] query count map {}", queryCountMap);

        ArrayList<AnalysisResult> results = new ArrayList<>();

        queryCountMap.forEach((query, count) -> {
            if (count > DUPLICATE_THRESHOLD) {
                results.add(
                        AnalysisResult.builder()
                                .type(ANALYZER_TYPE)
                                .severity(resolveSeverity(count))
                                .message("duplicate query detected")
                                .pattern(query)
                                .suggestions(buildSuggestions())
                                .occurrenceCount(count.intValue())
                                .build()
                );
            }
        });

        log.debug("[DuplicateQueryAnalyzer] final results - {}", results);

        return results;
    }

    private Severity resolveSeverity(long count) {
        if (count >= 5) return Severity.CRITICAL;
        if (count >= 3) return Severity.WARNING;
        return Severity.INFO;
    }

    private List<String> buildSuggestions() {
        return List.of(
                "Possible N+1 issue detected",
                "Use JOIN FETCH to reduce multiple queries",
                "Use @EntityGraph in repository",
                "Consider batch fetching"
        );
    }
}
