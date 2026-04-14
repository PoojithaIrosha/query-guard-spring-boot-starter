package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;
import io.poojithairosha.query_guard_spring_boot_starter.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class NPlusOneAnalyzer implements QueryAnalyzer {

    private static final int N_PLUS_ONE_THRESHOLD = 2;
    private static final String ANALYZER_TYPE = "N_PLUS_ONE";

    @Override
    public List<AnalysisResult> analyze(QueryContext context) {
        log.debug("[NPlusOneAnalyzer] start analyzing");
        if (context == null || context.getQueries().isEmpty()) {
            return Collections.emptyList();
        }

        List<QueryExecution> queries = context.getQueries();
        List<AnalysisResult> results = new ArrayList<>();

        QueryExecution rootQuery = findRootQuery(queries);

        if (rootQuery == null) {
            log.debug("[NPlusOneAnalyzer] root query not found. Skipping");
            return Collections.emptyList();
        }

        Map<String, Integer> patternCounts = new HashMap<>();

        for (QueryExecution query : queries) {
            String normalized = SqlUtil.normalizeSQL(query);

            if (isParameterizedQuery(normalized)) {
                patternCounts.merge(normalized, 1, Integer::sum);
            }
        }

        patternCounts.forEach((pattern, count) -> {
            if (count >= N_PLUS_ONE_THRESHOLD) {
                results.add(
                        AnalysisResult.builder()
                                .type(ANALYZER_TYPE)
                                .severity(Severity.WARNING)
                                .message("N+1 query detected")
                                .rootQuery(rootQuery.getSql())
                                .pattern(pattern)
                                .suggestions(buildSuggestions())
                                .occurrenceCount(count)
                                .build());
            }
        });

        log.debug("[NPlusOneAnalyzer] final results - {}", results);

        return results;
    }

    private List<String> buildSuggestions() {
        return List.of(
                "Use JOIN FETCH to fetch related entities in one query",
                "Use @EntityGraph on repository methods",
                "Consider batch fetching (hibernate.default_batch_fetch_size)"
        );
    }

    private QueryExecution findRootQuery(List<QueryExecution> queries) {
        return queries.stream()
                .filter(q -> !q.getSql().toLowerCase().contains("where"))
                .findFirst()
                .orElse(null);
    }

    private boolean isParameterizedQuery(String sql) {
        return sql.contains("where") && sql.contains("?");
    }
}
