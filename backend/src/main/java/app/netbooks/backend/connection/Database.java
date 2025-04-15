package app.netbooks.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class Database {
    @Getter
    private Connection connection;
    
    public Database(Environment environment) throws SQLException, SQLTimeoutException {
        String url = environment.getProperty("database.url");
        String username = environment.getProperty("database.username");
        String password = environment.getProperty("database.password");
        this.connection = DriverManager.getConnection(
            url, 
            username, 
            password
        );
    };
};
