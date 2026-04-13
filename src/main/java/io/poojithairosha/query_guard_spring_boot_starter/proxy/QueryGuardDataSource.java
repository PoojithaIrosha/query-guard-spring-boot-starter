package io.poojithairosha.query_guard_spring_boot_starter.proxy;

import io.poojithairosha.query_guard_spring_boot_starter.listener.QueryExecutionListener;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

public class QueryGuardDataSource implements DataSource {

    private final DataSource delegate;
    private final List<QueryExecutionListener> listeners;

    public QueryGuardDataSource(DataSource delegate, List<QueryExecutionListener> listeners) {
        this.delegate = delegate;
        this.listeners = listeners;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new QueryGuardConnection(delegate.getConnection(), listeners);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new QueryGuardConnection(delegate.getConnection(username, password), listeners);
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return delegate.createConnectionBuilder();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return delegate.createShardingKeyBuilder();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }
}
