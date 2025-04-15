package app.netbooks.backend.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseImpl implements Database {
    @Autowired
    private DataSource dataSource;
    
    @Override
    public Connection getConnection() throws SQLException, SQLTimeoutException {
        return this.dataSource.getConnection();
    };
};
