package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Access;
import app.netbooks.backend.models.User;

@Repository
public class UsersRepositoryImpl extends BaseRepository implements UsersRepository {
    @Autowired
    private BCryptPasswordEncoder encoder;

    public UsersRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public void initialize() {
        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
        ) {
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Users(\n" +
                "    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),\n" +
                "    name VARCHAR(120) NOT NULL,\n" +
                "    email VARCHAR(320) NOT NULL UNIQUE,\n" +
                "    password VARCHAR(60) NOT NULL,\n" +
                "    access INTEGER DEFAULT 0\n" +
                ");"
            );

            statement.execute(
                "DELETE FROM Users WHERE email = 'admin@gmail.com';"
            );

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Users(name, email, password, access) \n" +
                    "VALUES ('Admin', 'admin@gmail.com', ?, 1)\n" +
                    "ON CONFLICT DO NOTHING;"
                );
            ) {
                preparedStatement.setString(1, encoder.encode("admin"));
                preparedStatement.executeUpdate();
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };

    @Override
    public List<User> findAll() {
        List<User> persons = new ArrayList<User>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM Users;"
            );
        ) {
            while(result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String email = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                Access access = Access.fromValue(result.getInt("access"));
                User person = new User(uuid, name, email, password, access);
                persons.add(person);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return persons;
    };

    @Override
    public Optional<User> findById(UUID uuid) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Users WHERE uuid = ?;"
            );
        ) {
            statement.setObject(1, uuid);
            try (ResultSet result = statement.executeQuery();) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    Access access = Access.fromValue(result.getInt("access"));
                    User user = new User(uuid, name, email, password, access);
                    userFound = Optional.of(user);
                };

                return userFound;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    };

    @Override
    public Optional<User> findByEmail(String email) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Users WHERE email = ?;"
            );
        ) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    UUID uuid = UUID.fromString(result.getString("uuid"));
                    String name = result.getString("name");
                    String password = result.getString("password");
                    Access access = Access.fromValue(result.getInt("access"));
                    User user = new User(uuid, name, email, password, access);
                    userFound = Optional.of(user);
                };

                return userFound;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    };

    @Override
    public void create(User user) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Users(name, email, password, access) \n" +
                "VALUES (?, ?, ?, ?);"
            );
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getAccess().toValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void update(User user) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE Users SET (name, email, password, access) = (?, ?, ?, ?) WHERE uuid = ?;"
            );
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getAccess().toValue());
            statement.setObject(5, user.getUuid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void deleteById(UUID uuid) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Users WHERE uuid = ?;"
            );
        ) {
            statement.setObject(1, uuid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
};
