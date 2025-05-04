package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
    @DisplayName("Administrator")
    public void mustHaveAdministrator() {
        assertDoesNotThrow(() -> {
            List<User> users = repository.findAll();
            assertEquals(1, users.size());
            User user = users.get(0);
            assertEquals(Access.ADMINISTRATOR, user.getAccess());
        });
    };
};
