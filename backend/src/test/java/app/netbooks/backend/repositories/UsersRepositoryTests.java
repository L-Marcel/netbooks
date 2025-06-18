package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.netbooks.backend.BaseTests;
import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.UsersRepository;

public abstract class UsersRepositoryTests extends BaseTests {
    @Autowired
    public UsersRepository repository;

    @BeforeAll
    public static void clear(@Autowired Database database) {
        try (
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(
                "DELETE FROM user WHERE true;"
            );
            statement.executeUpdate(
                "INSERT IGNORE INTO user (uuid, name, email, password) VALUES\n" +
                "  ('3e78b287-399b-11f0-a0a8-7605c53cdf13', 'Administrador', 'admin@gmail.com', '$2a$10$iszhyLRMNIYy04weFz8dReJh3GxRsDv7hQYUBzaAAdn5WFc2csLXS');"
            );
            statement.executeUpdate(
                "INSERT IGNORE INTO admin (uuid) VALUES\n" +
                "  ('3e78b287-399b-11f0-a0a8-7605c53cdf13');"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };

    @Test
    @Order(1)
    @DisplayName("Create")
    public void mustCreate() {
        assertDoesNotThrow(() -> {
            repository.create(
                new User(
                    "Lucas",
                    null,
                    "lucas@gmail.com",
                    ""
                )
            );
        });
    };

    @Test
    @Order(2)
    @DisplayName("Find")
    public void mustFind() {
        assertDoesNotThrow(() -> {
            List<User> users = repository.findAll();
            users.removeIf(
                (User user) -> !user
                    .getEmail()
                    .equals("admin@gmail.com")
            );
            assertEquals(1, users.size());

            User user = users.get(0);
            Optional<User> userFound = repository.findById(
                user.getUuid()
            );
            assertTrue(userFound.isPresent());

            userFound = repository.findByEmail(user.getEmail());
            assertTrue(userFound.isPresent());
        });
    };

    @Test
    @Order(3)
    @DisplayName("Administrator")
    public void mustHaveAdministrator() {
        assertDoesNotThrow(() -> {
            Optional<User> user = repository.findByEmail("admin@gmail.com");
            assertTrue(user.isPresent());
        });
    };

    @Test
    @Order(4)
    @DisplayName("Update")
    public void mustUpdate() {
        assertDoesNotThrow(() -> {
            Optional<User> user = repository.findByEmail("lucas@gmail.com");
            assertTrue(user.isPresent());

            user.get().setName("Marcel");
            repository.update(user.get());

            Optional<User> userFound = repository.findById(user.get().getUuid());
            assertEquals("Marcel", userFound.get().getName());
        });
    };

    @Test
    @Order(5)
    @DisplayName("Delete")
    public void mustDelete() {
        assertDoesNotThrow( () -> {
            Optional<User> user = repository.findByEmail("lucas@gmail.com");
            assertTrue(user.isPresent());
            repository.deleteById(user.get().getUuid());

            user = repository.findById(user.get().getUuid());
            assertTrue(user.isEmpty());
        });
    };

    @Test
    @Order(6)
    @DisplayName("Exceptions")
    public void mustThrowExpections() throws SQLException {
        repository.create(
            new User(
                "Lucas",
                null,
                "lucas@gmail.com",
                ""
            )
        );

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
                    "Lucas",
                    null,
                    "lucas@gmail.com",
                    ""
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
                    null,
                    null,
                    "lucas@lucas.com",
                    ""
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
                    "Lucas",
                    null,
                    null,
                    ""
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
                    "Lucas",
                    null,
                    "lucas@lucas.com",
                    null
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            Optional<User> user = repository.findByEmail("lucas@gmail.com");
            assertTrue(user.isPresent());

            user.get().setName(null);
            repository.update(user.get());
        });

        assertThrows(InternalServerError.class, () -> {
            Optional<User> user = repository.findByEmail("lucas@gmail.com");
            assertTrue(user.isPresent());

            user.get().setEmail(null);
            repository.update(user.get());
        });

        assertThrows(InternalServerError.class, () -> {
            Optional<User> user = repository.findByEmail("lucas@gmail.com");
            assertTrue(user.isPresent());

            user.get().setPassword(null);
            repository.update(user.get());
        });

        Database database = mock(Database.class);
        when(database.getConnection()).thenThrow(
            new SQLException("Erro simulado de conexão!")
        );

        assertDoesNotThrow(() -> {
            UsersRepositoryImpl usersRepositoryImpl = new UsersRepositoryImpl(database);
            usersRepositoryImpl.initialize();
            usersRepositoryImpl.findAll();
            usersRepositoryImpl.findById(null);
            usersRepositoryImpl.findByEmail(null);
        });

        assertThrows(InternalServerError.class, () -> {
            UsersRepositoryImpl usersRepositoryImpl = new UsersRepositoryImpl(database);
            usersRepositoryImpl.deleteById(null);
        });
    };
};
