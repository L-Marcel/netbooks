package app.netbooks.backend.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import app.netbooks.backend.BaseTests;
import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;

public abstract class RoleTests extends BaseTests {
    @Test
    @Order(1)
    @DisplayName("Unknown")
    public void mustCreate() {
        List<Role> roles = new LinkedList<>();
        roles.add(Role.UNKNOWN);
        roles.add(Role.fromString("FAKE"));

        User user = new User(
            null, 
            null, 
            null, 
            null, 
            roles
        );

        assertEquals(0, user.getAuthorities().size());
    };
};
