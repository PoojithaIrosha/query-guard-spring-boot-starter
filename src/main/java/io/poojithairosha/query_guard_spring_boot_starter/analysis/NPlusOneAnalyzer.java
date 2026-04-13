package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import io.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class NPlusOneAnalyzer implements QueryAnalyzer {

    private static final int N_PLUS_ONE_THRESHOLD = 2;
    private static final Pattern WHERE_PATTERN = Pattern.compile("where\\s+.+?=\\s*\\?", Pattern.CASE_INSENSITIVE);

    @Override
    public List<AnalysisResult> analyze(QueryContext context) {
        if(context == null || context.getQueries().isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<QueryExecution>> groupedQueries = context.getQueries().stream().collect(Collectors.groupingBy(this::extractPattern));

        ArrayList<AnalysisResult> results = new ArrayList<>();

        groupedQueries.forEach((pattern, queries) -> {
            if(isCandidateForNPlusOne(pattern, queries)) {
                results.add(AnalysisResult.of(
                        "N_PLUS_ONE",
                        buildMessage(pattern, queries.size())
                ));
            }
        });

        return results;
    }

    private String extractPattern(QueryExecution execution) {
        String sql = execution.getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();

        // normalize parameters
        sql = sql.replaceAll("=\\s*\\?", "= ?");

        return sql;
    }

    private boolean isCandidateForNPlusOne(String pattern, List<QueryExecution> queries) {
        return queries.size() >= N_PLUS_ONE_THRESHOLD
                && WHERE_PATTERN.matcher(pattern).find();
    }

    private String buildMessage(String pattern, int count) {
        return String.format(
                "Possible N+1 detected (%d similar queries): %s",
                count,
                pattern
        );
    }
}
