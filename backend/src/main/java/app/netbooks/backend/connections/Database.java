package app.netbooks.backend.connections;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import org.springframework.stereotype.Component;

@Component
public interface Database {
    public Connection getConnection() throws SQLException, SQLTimeoutException;
};
