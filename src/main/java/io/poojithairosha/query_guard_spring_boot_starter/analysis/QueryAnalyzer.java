package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.poojithairosha.query_guard_spring_boot_starter.context.QueryContext;

import java.util.List;

public interface QueryAnalyzer {
    List<AnalysisResult> analyze(QueryContext context);
}
