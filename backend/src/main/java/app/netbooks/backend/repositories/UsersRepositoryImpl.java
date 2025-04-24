package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Access;
import app.netbooks.backend.models.User;

@Repository
public class UsersRepositoryImpl extends BaseRepository implements UsersRepository {
    public UsersRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public void initialize() {
        try {
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Users(\n" +
                "    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),\n" +
                "    name VARCHAR(120) NOT NULL,\n" +
                "    email VARCHAR(320) NOT NULL UNIQUE,\n" +
                "    password VARCHAR(24) NOT NULL,\n" +
                "    access INTEGER DEFAULT 0\n" +
                ");"
            );

            statement.execute(
                "INSERT INTO Users(email, password, name, access) \n" +
                "VALUES ('admin@gmail.com', 'admin', 'Admin', 1)\n" +
                "ON CONFLICT DO NOTHING;"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };

    @Override
    public List<User> findAll() {
        List<User> persons = new ArrayList<User>();

        try {
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT uuid, name, email, access FROM Users;"
            );

            while(result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String email = result.getString("email");
                String name = result.getString("name");
                Access access = Access.fromValue(result.getInt("access"));
                User person = new User(uuid, email, null, name, access);
                persons.add(person);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return persons;
    };
};
