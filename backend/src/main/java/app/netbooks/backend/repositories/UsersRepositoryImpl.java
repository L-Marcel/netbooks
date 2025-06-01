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
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.images.UserAvatarStorage;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.UsersRepository;

@Repository
public class UsersRepositoryImpl extends BaseRepository implements UsersRepository {
    @Autowired
    private UserAvatarStorage avatarStorage;

    public UsersRepositoryImpl(Database database) {
        super(database);
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
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String email = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                User person = new User(uuid, name, null, email, password);
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

            try (ResultSet result = statement.executeQuery();) {
                Optional<User> userFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    User user = new User(uuid, name, null, email, password);
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
                    UUID uuid = UUID.fromString(result.getString("uuid"));
                    String name = result.getString("name");
                    String password = result.getString("password");
                    User user = new User(uuid, name, null, email, password);
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
        ) {
            connection.setAutoCommit(false);

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO user (uuid, name, email, password) \n" +
                    "VALUES (?, ?, ?, ?);"
                );
            ) {
                statement.setString(1, user.getUuid().toString());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.executeUpdate();
            };

            if(user.getAvatar() != null) {
                avatarStorage.storeAvatar(user.getUuid(), user.getAvatar());
            };
            
            connection.commit();
        } catch (SQLException e) {
            avatarStorage.deleteAvatar(user.getUuid());
            throw new InternalServerError();
        }
    };

    @Override
    public void update(User user) {
        try (
            Connection connection = this.database.getConnection();
        ) {
            connection.setAutoCommit(false);

            try (
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
            };

            if(user.getAvatar() != null) {
                avatarStorage.storeAvatar(user.getUuid(), user.getAvatar());
            } else {
                avatarStorage.deleteAvatar(user.getUuid());
            };

            connection.commit();
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
