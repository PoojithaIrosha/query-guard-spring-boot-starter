package io.github.poojithairosha.query_guard_spring_boot_starter;

import io.github.poojithairosha.query_guard_spring_boot_starter.config.QueryGuardConfiguration;
import io.github.poojithairosha.query_guard_spring_boot_starter.proxy.QueryGuardDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class QueryGuardSpringBootStarterApplicationTests {

	@Test
	void wrapsExistingDataSourceWithoutRegisteringAnotherDataSourceBean() {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
			context.registerBean(
					"queryGuardDataSourcePostProcessor",
					BeanPostProcessor.class,
					QueryGuardConfiguration::queryGuardDataSourcePostProcessor
			);
			context.register(TestDataSourceConfiguration.class);
			context.refresh();

			assertThat(context.getBean(DataSource.class)).isInstanceOf(QueryGuardDataSource.class);
			assertThat(context.getBeanNamesForType(DataSource.class)).containsExactly("dataSource");
		}
	}

	@Configuration(proxyBeanMethods = false)
	static class TestDataSourceConfiguration {

		@Bean
		DataSource dataSource() {
			return new StubDataSource();
		}

	}

	static class StubDataSource implements DataSource {

		@Override
		public Connection getConnection() throws SQLException {
			throw new SQLFeatureNotSupportedException();
		}

		@Override
		public Connection getConnection(String username, String password) throws SQLException {
			throw new SQLFeatureNotSupportedException();
		}

		@Override
		public PrintWriter getLogWriter() {
			return null;
		}

		@Override
		public void setLogWriter(PrintWriter out) {
		}

		@Override
		public void setLoginTimeout(int seconds) {
		}

		@Override
		public int getLoginTimeout() {
			return 0;
		}

		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			throw new SQLFeatureNotSupportedException();
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			if (iface.isInstance(this)) {
				return iface.cast(this);
			}
			throw new SQLException("Not a wrapper for " + iface.getName());
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) {
			return iface.isInstance(this);
		}

	}

}
