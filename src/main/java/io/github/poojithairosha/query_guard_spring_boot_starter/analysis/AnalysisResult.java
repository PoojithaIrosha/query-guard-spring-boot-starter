package io.github.poojithairosha.query_guard_spring_boot_starter.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AnalysisResult {
    private final String type;
    private final Severity severity;
    private final String message;
    private final String rootQuery;
    private final String pattern;
    private final List<String> suggestions;
    private int occurrenceCount;
}

