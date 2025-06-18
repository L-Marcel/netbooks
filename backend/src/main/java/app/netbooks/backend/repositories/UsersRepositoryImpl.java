package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.UsersRepository;

@Repository
public class UsersRepositoryImpl extends BaseRepository implements UsersRepository {
    public UsersRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<User> findAll() {
        return this.queryOrDefault((connection) -> {
            List<User> persons = new ArrayList<>();

            try (
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
            };

            return persons;
        }, new ArrayList<>());
    };

    @Override
    public Optional<User> findById(UUID uuid) {
        return this.queryOrDefault((connection) -> {
            Optional<User> userFound = Optional.empty();
        
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user WHERE uuid = ?;"
                );
            ) {
                statement.setString(1, uuid.toString());

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        String name = result.getString("name");
                        String email = result.getString("email");
                        String password = result.getString("password");

                        User user = new User(uuid, name, null, email, password);

                        userFound = Optional.of(user);
                    };  
                };
            };

            return userFound;
        }, Optional.empty());
    };

    @Override
    public Optional<User> findByEmail(String email) {
        return this.queryOrDefault((connection) -> {
            Optional<User> userFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user WHERE email = ?;"
                );
            ) {
                statement.setString(1, email);

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        UUID uuid = UUID.fromString(result.getString("uuid"));
                        String name = result.getString("name");
                        String password = result.getString("password");

                        User user = new User(uuid, name, null, email, password);

                        userFound = Optional.of(user);
                    };
                };
            };

            return userFound;
        }, Optional.empty());
    };

    @Override
    public void create(User user) {
        this.execute((connection) -> {
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
        });
    };

    @Override
    public void update(User user) {
        this.execute((connection) -> {
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
        });
    };

    @Override
    public void deleteById(UUID uuid) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM user WHERE uuid = ?;"
                );
            ) {
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            };
        });
    };
};
