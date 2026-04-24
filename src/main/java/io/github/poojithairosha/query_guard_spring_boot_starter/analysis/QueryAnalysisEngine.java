package io.github.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.github.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QueryAnalysisEngine {

    private final List<QueryAnalyzer> analyzers;

    public List<AnalysisResult> analyze(QueryContext context) {
        return analyzers.stream()
                .flatMap(a -> a.analyze(context).stream())
                .collect(Collectors.toList());
    }
}
