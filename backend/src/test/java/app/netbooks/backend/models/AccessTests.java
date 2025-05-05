package app.netbooks.backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import app.netbooks.backend.BaseTests;

public abstract class AccessTests extends BaseTests {
    @Test
    @Order(1)
    @DisplayName("Default")
    public void mustReturnDefault() {
        assertEquals(Access.DEFAULT, Access.fromValue(0));
        assertEquals(Access.DEFAULT, Access.fromValue(3));
        assertEquals(Access.DEFAULT, Access.fromValue(-1));
    }; 
};
