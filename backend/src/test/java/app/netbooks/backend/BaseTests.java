package app.netbooks.backend;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import app.netbooks.backend.connections.interfaces.Database;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    useMainMethod = SpringBootTest.UseMainMethod.ALWAYS
)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class BaseTests {
    @Autowired
    protected Database database;

    public static void clear(Database database, String table) throws SQLException {
        database.execute((connection) -> {
              try (
                Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DELETE FROM " + table + ";");
            };
        });
    };
};