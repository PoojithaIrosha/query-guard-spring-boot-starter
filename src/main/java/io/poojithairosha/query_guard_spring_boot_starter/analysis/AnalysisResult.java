package io.poojithairosha.query_guard_spring_boot_starter.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AnalysisResult {

    private final String type;
    private final String message;

    public static AnalysisResult of(String type, String message) {
        return new AnalysisResult(type, message);
    }
}
