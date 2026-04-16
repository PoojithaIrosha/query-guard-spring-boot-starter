package io.github.poojithairosha.query_guard_spring_boot_starter.context;

public class QueryContextHolder {

    private QueryContextHolder() {}

    private static final ThreadLocal<QueryContext> CONTEXT = new ThreadLocal<>();

    public static void init() {
        CONTEXT.set(new QueryContext());
    }

    public static QueryContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
