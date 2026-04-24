# Query Guard

Query Guard is a Spring Boot starter that tracks JDBC queries executed during an HTTP request and reports query patterns that can indicate inefficient data access.

The main problem it targets is query explosion: a single request causing many database round trips because application code loads data one record or relationship at a time. A common form is the N+1 query problem, where one root query loads N parent records and then additional queries are executed for each parent record or related entity. This usually increases latency, database load, connection pool pressure, and tail response times.

Query Guard wraps the application `DataSource`, records executed SQL statements in a request-local context, analyzes them after the servlet request completes, logs a structured report, stores recent traces in memory, and optionally publishes Micrometer counters.

## Features

- Spring Boot auto-configuration through `AutoConfiguration.imports`.
- JDBC query tracking through a primary `DataSource` wrapper.
- Per-request query collection using a servlet `Filter` and `ThreadLocal` request context.
- Duplicate query detection based on normalized SQL text.
- N+1 detection based on repeated parameterized `WHERE ... ?` query patterns after a root query.
- Structured JSON report logging to the `QUERY_GUARD` logger.
- Spring application event publication for each report.
- Micrometer counter integration when a `MeterRegistry` bean is available.
- In-memory request trace storage with a default capacity of 1000 traces.
- REST API for reading stored request traces.
- Built-in dashboard and query explorer served from `/queryguard-ui`.
- Configurable enablement, analyzer toggles, logging toggle, and path exclusions.
- Request query-count threshold property is present, but threshold-based warning behavior is not currently implemented.
- AOP annotations, custom application annotations, transaction interceptors, and Hibernate-specific listeners are not currently implemented.

## How It Works

Query Guard is enabled by default when the starter is on the classpath.

At startup, `QueryGuardAutoConfiguration` imports the library configuration classes. When `queryguard.enabled=true`, Query Guard wraps existing `DataSource` beans with a proxy that delegates to the original `DataSource`. Connections, statements, and prepared statements returned from that data source are wrapped by Query Guard proxy classes.

For each servlet request, `QueryGuardFilter` initializes a `QueryContext` in a `ThreadLocal`. When application code executes JDBC statements or prepared statements through the wrapped `DataSource`, Query Guard records:

- SQL text
- execution duration in milliseconds
- start and end timestamps in nanoseconds

After the request completes, `QueryGuardExecutor` runs the configured analyzers:

- `DuplicateQueryAnalyzer` groups normalized SQL and reports SQL executed more than once.
- `NPlusOneAnalyzer` looks for a root query without `WHERE`, then reports repeated normalized parameterized queries containing `WHERE` and `?` when the same pattern appears at least twice.

The processed results are converted into a `QueryGuardReport`, saved to in-memory trace storage, logged as JSON, and published as a `QueryGuardEvent`. If Micrometer is available, the event listener updates Query Guard counters.

## Compatibility

| Query Guard Version | Supported Spring Boot |
|---------------------|----------------------|
| 1.x.x-sb2           | 2.x                  |
| 1.x.x               | 3.x                  |
| 2.x.x               | 4.x                  |

## Installation

### Maven

Use the dependency that matches your application:

```xml
<!-- Spring Boot 2.x -->
<dependency>
    <groupId>io.github.poojithairosha</groupId>
    <artifactId>query-guard-spring-boot-starter</artifactId>
    <version>1.0.0-sb2</version>
</dependency>

<!-- Spring Boot 3.x -->
<dependency>
    <groupId>io.github.poojithairosha</groupId>
    <artifactId>query-guard-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- Spring Boot 4.x -->
<dependency>
    <groupId>io.github.poojithairosha</groupId>
    <artifactId>query-guard-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

For local development:

```bash
./mvnw clean install
```

## Quick Start

1. Add the starter dependency to a Spring Boot web application that uses a JDBC-backed `DataSource`.
2. Configure Query Guard. At minimum, define `queryguard.tracing.exclude-paths`; the current implementation expects this list to be non-null.
3. Run the application.
4. Execute HTTP requests that hit database-backed endpoints.
5. Check application logs, `/queryguard/api/requests`, or the built-in dashboard and query explorer at `/queryguard-ui`.

Expected startup behavior:

```text
QueryGuard DataSource Initialized
```

Expected query logging behavior:

```text
Executing Query - select e1_0.id,e1_0.name from enrollment e1_0
Query executed in 3.42 ms
```

Expected report logging behavior:

```json
{
  "type": "QUERY_GUARD",
  "endpoint": "/api/enrollments",
  "method": "GET",
  "queryCount": 25,
  "totalExecutionTimeMs": 118,
  "overallSeverity": "CRITICAL",
  "nplusOneDetected": true,
  "issues": [
    {
      "type": "N_PLUS_ONE",
      "severity": "WARNING",
      "message": "N+1 query detected",
      "pattern": "select c1_0.id,c1_0.name from course c1_0 where c1_0.id = ?",
      "suggestions": [
        "Use JOIN FETCH to fetch related entities in one query",
        "Use @EntityGraph on repository methods",
        "Consider batch fetching (hibernate.default_batch_fetch_size)"
      ],
      "occurrenceCount": 24
    }
  ],
  "traceId": "cfa0124f-0b01-439b-a42d-630cd36a2f5e",
  "timestamp": 1710000000000
}
```

## Configuration

The configuration prefix is `queryguard`.

```yaml
queryguard:
  enabled: true
  max-queries-per-request: 50
  analyzers:
    duplicate: true
    n-plus-one: true
  logging:
    enabled: true
    level: WARNING
  tracing:
    exclude-paths:
      - /actuator/**
      - /favicon.ico
```

| Property | Default | Description |
| --- | --- | --- |
| `queryguard.enabled` | `true` | Enables Query Guard auto-configured beans, including the `DataSource` wrapper, servlet filter, trace API, UI controller, logger, and report builder. |
| `queryguard.max-queries-per-request` | `50` | Configuration property exists, but no analyzer or warning currently uses it. |
| `queryguard.analyzers.duplicate` | `true` | Enables `DuplicateQueryAnalyzer`. |
| `queryguard.analyzers.n-plus-one` | `true` | Enables `NPlusOneAnalyzer`. |
| `queryguard.logging.enabled` | `true` | Enables structured report logging after each traced request. |
| `queryguard.logging.level` | `WARNING` | Configuration property exists, but the current logger always writes reports with `log.info(...)`. |
| `queryguard.tracing.exclude-paths` | `null` | Paths excluded from request tracing. The current implementation expects this list to be configured. |

Query Guard automatically excludes these paths at runtime:

```text
/queryguard/**
/queryguard-ui/**
```

Path matching is currently implemented as a prefix check after removing a trailing `/**` from the configured pattern.

## Metrics & Monitoring

Micrometer integration is available when `io.micrometer.core.instrument.MeterRegistry` is on the classpath and a `MeterRegistry` bean exists.

The starter registers these counters:

| Metric | Description |
| --- | --- |
| `queryguard_requests_total` | Total Query Guard reports processed by the metrics listener. |
| `queryguard_nplusone_total` | Total N+1 occurrences reported by Query Guard events. |
| `queryguard_critical_total` | Total reports whose severity is `CRITICAL`. |

The project includes `spring-boot-starter-actuator` and `micrometer-registry-prometheus` as dependencies. To expose Prometheus metrics in a consuming application, configure Spring Boot Actuator endpoint exposure, for example:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  endpoint:
    prometheus:
      access: unrestricted
```

Then scrape:

```text
/actuator/prometheus
```

The bundled UI includes a live dashboard that reads Prometheus text output from `/actuator/prometheus`. Historical time ranges in the UI are built from repeated client-side polling, not from Prometheus range queries.

## Dashboard, Query Explorer & API

Query Guard comes with a built-in dashboard and query explorer served from:

```text
/queryguard-ui
```

The dashboard uses stored request traces and Query Guard metrics to inspect recent requests, N+1 detections, duplicate SQL patterns, query timings, and per-trace query details.

Query Guard exposes the following endpoints when enabled:

| Endpoint | Description |
| --- | --- |
| `GET /queryguard/api/requests` | Returns all request traces currently stored in memory. |
| `GET /queryguard/api/requests/{traceId}` | Returns one request trace by trace ID, or throws an exception when not found. |
| `GET /queryguard-ui` | Serves the built-in dashboard and query explorer. |
| `GET /queryguard-ui/dashboard` | Serves the dashboard route. |
| `GET /queryguard-ui/trace/{traceId}` | Serves the query explorer trace detail route. |

Request traces are stored in `InMemoryTraceStorage` by default. The store keeps up to 1000 traces and evicts the eldest entry when the limit is exceeded.

Applications can provide their own `TraceStorage` bean to replace the default in-memory implementation.

## Example Output

Example duplicate query issue:

```json
{
  "type": "DUPLICATE_QUERY",
  "severity": "WARNING",
  "message": "duplicate query detected",
  "pattern": "select s1_0.id,s1_0.name from student s1_0 where s1_0.id = ?",
  "suggestions": [
    "Possible N+1 issue detected",
    "Use JOIN FETCH to reduce multiple queries",
    "Use @EntityGraph in repository",
    "Consider batch fetching"
  ],
  "occurrenceCount": 3
}
```

Example N+1 issue:

```text
[QueryGuard] Potential N+1 detected
Endpoint: /api/enrollments
Method: GET
Total Queries: 25
Pattern: select c1_0.id,c1_0.name from course c1_0 where c1_0.id = ?
Occurrences: 24
Severity: CRITICAL
```

The plain-text format above is illustrative. The current implementation logs the report as JSON through the `QUERY_GUARD` logger.

## Architecture

This repository is currently a single Maven module. It is organized by package rather than by separate Maven modules.

| Package | Responsibility |
| --- | --- |
| `config` | Spring Boot auto-configuration, properties, web, analysis, logging, and metrics bean registration. |
| `proxy` | JDBC `DataSource`, `Connection`, `Statement`, and `PreparedStatement` wrappers. |
| `filter` | Servlet filter entry point for per-request tracing. |
| `context` | Request-local query context stored in a `ThreadLocal`. |
| `listener` | Query execution listeners and metrics event listener. |
| `analysis` | Duplicate query and N+1 analyzers. |
| `report` | Report and issue models plus report builder. |
| `event` | Spring application event published after report logging. |
| `metrics` | Micrometer counter registration and updates. |
| `storage` | Trace storage abstraction and default in-memory implementation. |
| `controller` | Request trace API and UI forwarding controller. |
| `static/queryguard-ui` | Bundled frontend assets. |

Spring Boot registration is provided by:

```text
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

which imports:

```text
config.io.github.poojithairosha.query_guard_spring_boot_starter.QueryGuardAutoConfiguration
```

## Contributing

1. Open an issue or describe the problem before making broad behavioral changes.
2. Keep changes focused and aligned with the existing package structure.
3. Add or update tests for analyzers, auto-configuration, JDBC proxy behavior, API endpoints, and metrics when behavior changes.
4. Run the test suite before submitting changes:

```bash
./mvnw test
```

5. Document new configuration properties and endpoint changes in this README.

## License

![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
