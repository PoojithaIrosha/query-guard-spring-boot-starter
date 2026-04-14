package io.poojithairosha.query_guard_spring_boot_starter.storage;

import io.poojithairosha.query_guard_spring_boot_starter.model.RequestTrace;

import java.util.*;

public class InMemoryTraceStorage implements TraceStorage {

    private final int maxSize;
    private final Map<String, RequestTrace> storage;

    public InMemoryTraceStorage(int maxSize) {
        this.maxSize = maxSize;

        this.storage = Collections.synchronizedMap(
                new LinkedHashMap<String, RequestTrace>(16, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, RequestTrace> eldest) {
                        return size() > InMemoryTraceStorage.this.maxSize;
                    }
                }
        );
    }

    @Override
    public void save(RequestTrace trace) {
        storage.put(trace.getTraceId(), trace);
    }

    @Override
    public Optional<RequestTrace> get(String traceId) {
        return Optional.ofNullable(storage.get(traceId));
    }

    @Override
    public List<RequestTrace> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
