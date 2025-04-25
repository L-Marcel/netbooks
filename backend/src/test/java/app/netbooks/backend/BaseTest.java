package app.netbooks.backend;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import app.netbooks.backend.connections.Database;

@ExtendWith(SpringExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public abstract class BaseTest  {
    public static void clear(Database database, String table) {
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + table + ";");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };
};