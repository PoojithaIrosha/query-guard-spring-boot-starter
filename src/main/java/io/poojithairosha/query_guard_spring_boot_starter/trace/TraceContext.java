package io.poojithairosha.query_guard_spring_boot_starter.trace;

import org.slf4j.MDC;

import java.util.UUID;

public class TraceContext {
    private static final String TRACE_ID = "traceId";

    public static String getOrCreateTraceId() {
        String traceId = MDC.get(TRACE_ID);

        if (traceId != null) {
            return traceId;
        }

        traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        return traceId;
    }

    public static void clear(boolean created) {
        if (created) {
            MDC.remove(TRACE_ID);
        }
    }
}
