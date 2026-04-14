package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import io.poojithairosha.query_guard_spring_boot_starter.util.SqlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryResultProcessor {

    private QueryResultProcessor() {}

    public static List<AnalysisResult> process(List<AnalysisResult> results) {

        Map<String, List<AnalysisResult>> grouped = results.stream()
                .collect(Collectors.groupingBy(r -> SqlUtil.normalizeSQL(r.getPattern())));

        List<AnalysisResult> finalResults = new ArrayList<>();

        for (Map.Entry<String, List<AnalysisResult>> entry : grouped.entrySet()) {

            List<AnalysisResult> group = entry.getValue();

            boolean hasNPlusOne = group.stream()
                    .anyMatch(r -> "N_PLUS_ONE".equals(r.getType()));

            if (hasNPlusOne) {
                group.stream()
                        .filter(r -> "N_PLUS_ONE".equals(r.getType()))
                        .findFirst()
                        .ifPresent(finalResults::add);
            } else {
                finalResults.addAll(group);
            }
        }

        return finalResults;
    }

}
