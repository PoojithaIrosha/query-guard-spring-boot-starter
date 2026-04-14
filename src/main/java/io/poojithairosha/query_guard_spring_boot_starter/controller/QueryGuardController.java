package io.poojithairosha.query_guard_spring_boot_starter.controller;

import io.poojithairosha.query_guard_spring_boot_starter.model.RequestTrace;
import io.poojithairosha.query_guard_spring_boot_starter.storage.TraceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/queryguard/api")
@RequiredArgsConstructor
public class QueryGuardController {

    private final TraceStorage traceStorage;

    @GetMapping("/requests")
    public List<RequestTrace> getAllRequests() {
        return traceStorage.getAll();
    }

    @GetMapping("/requests/{traceId}")
    public RequestTrace getRequest(@PathVariable String traceId) {
        return traceStorage.get(traceId)
                .orElseThrow(() -> new RuntimeException("Trace not found"));
    }
}
