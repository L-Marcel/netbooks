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
import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;

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
                "DELETE FROM user WHERE email != 'admin@gmail.com';"
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
                (User user) -> user
                    .getRoles()
                    .contains(Role.ADMINISTRATOR)
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
            assertTrue(user.get().getRoles().contains(Role.ADMINISTRATOR));
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
                "lucas@gmail.com",
                ""
            )
        );

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
                    "Lucas",
                    "lucas@gmail.com",
                    ""
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
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
                    ""
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new User(
                    "Lucas",
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
            usersRepositoryImpl.findAllRoles();
            usersRepositoryImpl.findRoles(null);
            usersRepositoryImpl.findById(null);
            usersRepositoryImpl.findByEmail(null);
        });

        assertThrows(InternalServerError.class, () -> {
            UsersRepositoryImpl usersRepositoryImpl = new UsersRepositoryImpl(database);
            usersRepositoryImpl.deleteById(null);
        });
    };
};
