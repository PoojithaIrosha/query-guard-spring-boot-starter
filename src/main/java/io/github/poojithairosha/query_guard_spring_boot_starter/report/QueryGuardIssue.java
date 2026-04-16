package io.github.poojithairosha.query_guard_spring_boot_starter.report;

import io.github.poojithairosha.query_guard_spring_boot_starter.analysis.Severity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class QueryGuardIssue {

    private final String type;
    private final Severity severity;
    private final String message;
    private final String pattern;
    private final List<String> suggestions;
    private final int occurrenceCount;

}
