package app.netbooks.backend.connections;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Connections {
    @Autowired
    private DataSource dataSource;

    private static final ThreadLocal<Connection> transactionalConnection = new ThreadLocal<>();
    
    protected Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    };

    protected void clearTransactionConnection() {
        Connections.transactionalConnection.remove();
    };

    protected Connection getTransactionConnection() throws SQLException {
        if(Connections.transactionalConnection.get() == null) {
            Connection connection = this.getConnection();
            Connections.transactionalConnection.set(connection);
        };

        return Connections.transactionalConnection.get();
    };

    protected boolean haveTransactionRunning() {
        return Connections.transactionalConnection.get() != null;
    };
}
