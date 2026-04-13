package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QueryAnalysisEngine {

    private final List<QueryAnalyzer> analyzers;

    public List<AnalysisResult> analyze(QueryContext context) {
        return analyzers.stream()
                .flatMap(a -> a.analyze(context).stream())
                .toList();
    }
}
