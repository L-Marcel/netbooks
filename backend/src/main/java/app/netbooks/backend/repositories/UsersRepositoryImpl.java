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
            List<User> users = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM user_as_subscriber;
                    """
                );
            ) {
                while(result.next()) {
                    UUID uuid = UUID.fromString(result.getString("uuid"));
                    String email = result.getString("email");
                    String name = result.getString("name");
                    String password = result.getString("password");
                    Boolean automaticBilling = result.getBoolean("automatic_billing");

                    User user = new User(
                        uuid, 
                        name, 
                        null, 
                        email, 
                        password, 
                        automaticBilling
                    );
                    
                    users.add(user);
                };
            };

            return users;
        }, new ArrayList<>());
    };

    @Override
    public Optional<User> findById(UUID uuid) {
        return this.queryOrDefault((connection) -> {
            Optional<User> userFound = Optional.empty();
        
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM user_as_subscriber
                    WHERE uuid = ?;
                    """
                );
            ) {
                statement.setString(1, uuid.toString());

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        String name = result.getString("name");
                        String email = result.getString("email");
                        String password = result.getString("password");
                        Boolean automaticBilling = result.getBoolean("automatic_billing");

                        User user = new User(
                            uuid, 
                            name, 
                            null, 
                            email, 
                            password, 
                            automaticBilling
                        );

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
                    // language=sql
                    """
                    SELECT * FROM user_as_subscriber
                    WHERE email = ?;
                    """
                );
            ) {
                statement.setString(1, email);

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        UUID uuid = UUID.fromString(result.getString("uuid"));
                        String name = result.getString("name");
                        String password = result.getString("password");
                        Boolean automaticBilling = result.getBoolean("automatic_billing");

                        User user = new User(
                            uuid, 
                            name, 
                            null, 
                            email, 
                            password, 
                            automaticBilling
                        );

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
                    // language=sql
                    """
                    INSERT INTO user (uuid, name, email, password)
                    VALUES (?, ?, ?, ?);
                    """
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
                    // language=sql
                    """
                    UPDATE user
                    SET name = ?,
                        email = ?,
                        password = ?
                    WHERE uuid = ?;
                    """
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
                    // language=sql
                    """
                    DELETE FROM user WHERE uuid = ?;
                    """
                );
            ) {
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void switchAutomaticBillingById(UUID uuid) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE subscriber
                    SET automatic_billing = NOT automatic_billing
                    WHERE uuid = ?;
                    """
                );
            ) {
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            };
        });
    };
};
