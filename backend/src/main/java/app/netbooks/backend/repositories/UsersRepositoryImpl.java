package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Role;
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
        ) {
            connection.setAutoCommit(false);

            try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedCreateStatement = connection.prepareStatement(
                    "INSERT INTO user (uuid, name, email, password)\n" +
                    "VALUES (UUID(), 'Admin', 'admin@gmail.com', ?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "    name = VALUES(name),\n" +
                    "    password = VALUES(password);"
                );
                PreparedStatement preparedSetAdminStatement = connection.prepareStatement(
                    "INSERT INTO admin (uuid)\n" +
                    "SELECT * FROM (SELECT ?) AS tmp\n" +
                    "WHERE NOT EXISTS (\n" +
                    "    SELECT 1 FROM admin WHERE uuid = ?\n" +
                    ") LIMIT 1;"
                );
            ) {
                preparedCreateStatement.setString(1, encoder.encode("admin"));
                preparedCreateStatement.executeUpdate();

                try (
                    ResultSet result = statement.executeQuery(
                        "SELECT * FROM user WHERE email = 'admin@gmail.com';"
                    );
                ) {
                    if(result.next()) {
                        UUID uuid = UUID.fromString(result.getString("uuid"));
                        preparedSetAdminStatement.setString(1, uuid.toString());
                        preparedSetAdminStatement.setString(2, uuid.toString());
                        preparedSetAdminStatement.executeUpdate();
                    };
                }
              
                connection.commit();
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };

    private Map<UUID, List<Role>> findAllRoles(Connection connection) {
        Map<UUID, List<Role>> rolesMap = new LinkedHashMap<>();

        try (
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM user_roles;"
            );
        ) {
            while(result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                Role role = Role.fromString(result.getString("role"));
                rolesMap.computeIfAbsent(
                    uuid,
                    v -> new ArrayList<Role>()
                ).add(role);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return rolesMap;
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
            Map<UUID, List<Role>> allRoles = this.findAllRoles(connection);
            while(result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String email = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                List<Role> roles = allRoles.getOrDefault(uuid, new LinkedList<>());
                User person = new User(uuid, name, email, password, roles);
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
            statement.setString(1, uuid.toString());

            Map<UUID, List<Role>> allRoles = this.findAllRoles(connection);
            try (ResultSet result = statement.executeQuery();) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    List<Role> roles = allRoles.getOrDefault(uuid, new LinkedList<>());
                    User user = new User(uuid, name, email, password, roles);
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

            Map<UUID, List<Role>> allRoles = this.findAllRoles(connection);
            try (ResultSet result = statement.executeQuery()) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    UUID uuid = UUID.fromString(result.getString("uuid"));
                    String name = result.getString("name");
                    String password = result.getString("password");
                    List<Role> roles = allRoles.getOrDefault(uuid, new LinkedList<>());
                    User user = new User(uuid, name, email, password, roles);
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
                "INSERT INTO user (uuid, name, email, password) \n" +
                "VALUES (UUID(), ?, ?, ?);"
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
            statement.setString(4, user.getUuid().toString());
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
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };
};
