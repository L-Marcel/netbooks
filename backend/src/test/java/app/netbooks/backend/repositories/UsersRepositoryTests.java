package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.netbooks.backend.BaseTests;
import app.netbooks.backend.models.Access;
import app.netbooks.backend.models.User;

public abstract class UsersRepositoryTests extends BaseTests {
    @Autowired
    public UsersRepository repository;

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
            users.removeIf((User user) -> user.getAccess() != Access.DEFAULT);
            assertEquals(1, users.size());

            User user = users.get(0);
            Optional<User> userFound = repository.findById(user.getUuid());
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
            assertEquals(Access.ADMINISTRATOR, user.get().getAccess());
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
            
            user.get().setAccess(Access.SUBSCRIBER);
            repository.update(user.get());
            userFound = repository.findById(user.get().getUuid());
            assertEquals(Access.SUBSCRIBER, userFound.get().getAccess());
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

            user = repository.findByEmail("lucas@gmail.com");
            assertTrue(user.isEmpty());
        });
    };
};
