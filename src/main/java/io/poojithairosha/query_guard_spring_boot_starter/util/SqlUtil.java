package io.poojithairosha.query_guard_spring_boot_starter.util;

import io.poojithairosha.query_guard_spring_boot_starter.model.QueryExecution;

public class SqlUtil {

    public static String normalizeSQL(String sql) {
        return sql == null ? null :
                sql.toLowerCase()
                        .replaceAll("\\s+", " ")
                        .replaceAll("=\\s*\\?", "= ?")
                        .trim();
    }

    public static String normalizeSQL(QueryExecution execution) {
        return normalizeSQL(execution.getSql());
    }
}
