package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;
import app.netbooks.backend.utils.UUIDUtils;

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
        ) {
            connection.setAutoCommit(false);

            try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedCreateStatement = connection.prepareStatement(
                    "INSERT IGNORE INTO user (name, email, password) \n" +
                    "VALUES ('Admin', 'admin@gmail.com', ?);"
                );
                PreparedStatement preparedSetAdminStatement = connection.prepareStatement(
                    "INSERT IGNORE INTO admin (uuid) \n" +
                    "VALUES (?);"
                );
            ) {
                statement.execute(
                    "DELETE FROM user WHERE email = 'admin@gmail.com';"
                );

                preparedCreateStatement.setString(1, encoder.encode("admin"));
                preparedCreateStatement.executeUpdate();

                byte[] bytes = {};
                
                try (
                    ResultSet result = statement.executeQuery(
                        "SELECT * FROM user WHERE email = 'admin@gmail.com';"
                    );
                ) {
                    if(result.next()) {
                        bytes = result.getBytes("uuid");
                    };
                }
                
                preparedSetAdminStatement.setBytes(1, bytes);
                connection.commit();
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
                "SELECT * FROM user;"
            );
        ) {
            while(result.next()) {
                byte[] bytes = result.getBytes("uuid");
                UUID uuid = UUIDUtils.fromBytes(bytes);
                String email = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                // Access access = Access.fromValue(result.getInt("access"));
                User person = new User(uuid, name, email, password, new LinkedList<Role>());
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
                "SELECT * FROM user WHERE uuid = ?;"
            );
        ) {
            statement.setBytes(1, UUIDUtils.toBytes(uuid));
            try (ResultSet result = statement.executeQuery();) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    User user = new User(uuid, name, email, password, new LinkedList<Role>());
                    userFound = Optional.of(user);
                };

                return userFound;
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    };

    @Override
    public Optional<User> findByEmail(String email) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM user WHERE email = ?;"
            );
        ) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    byte[] bytes = result.getBytes("uuid");
                    UUID uuid = UUIDUtils.fromBytes(bytes);
                    String name = result.getString("name");
                    String password = result.getString("password");
                    User user = new User(uuid, name, email, password, new LinkedList<Role>());
                    userFound = Optional.of(user);
                };

                return userFound;
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    };

    @Override
    public void create(User user) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO user (name, email, password) \n" +
                "VALUES (?, ?, ?);"
            );
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };

    @Override
    public void update(User user) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE user\n" +
                "SET name = ?,\n" +
                "    email = ?,\n" +
                "    password = ?\n" +
                "WHERE uuid = ?;"
            );
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, UUIDUtils.toBytes(user.getUuid()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };

    @Override
    public void deleteById(UUID uuid) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM user WHERE uuid = ?;"
            );
        ) {
            statement.setBytes(1, UUIDUtils.toBytes(uuid));
            statement.executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };
};
