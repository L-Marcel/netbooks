package app.netbooks.backend.configurations;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource(Environment environment) {
        String url = environment.getProperty("database.url");
        String username = environment.getProperty("database.username");
        String password = environment.getProperty("database.password");
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(url);
        configuration.setUsername(username);
        configuration.setPassword(password);
        configuration.setMaximumPoolSize(12);
        configuration.setIdleTimeout(10 * 60 * 1000);
        configuration.setConnectionTimeout(30 * 1000);
        return new HikariDataSource(configuration);
    };
};
