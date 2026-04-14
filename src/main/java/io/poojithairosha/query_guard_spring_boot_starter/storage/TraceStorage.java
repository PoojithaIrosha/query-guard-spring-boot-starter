package io.poojithairosha.query_guard_spring_boot_starter.storage;

import io.poojithairosha.query_guard_spring_boot_starter.model.RequestTrace;

import java.util.List;
import java.util.Optional;

public interface TraceStorage {

    void save(RequestTrace trace);

    Optional<RequestTrace> get(String traceId);

    List<RequestTrace> getAll();

    void clear();

}
