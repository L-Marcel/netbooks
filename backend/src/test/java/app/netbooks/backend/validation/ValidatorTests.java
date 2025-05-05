package app.netbooks.backend.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import app.netbooks.backend.BaseTests;
import app.netbooks.backend.errors.ValidationsError;

public abstract class ValidatorTests extends BaseTests {
    @Test
    @Order(1)
    @DisplayName("Walkover")
    public void mustValidateWalkover() {
        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("any", null);
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("any", null).nullable();
            validator.run();
        });
    };

    @Test
    @Order(2)
    @DisplayName("Min")
    public void mustValidateMin() {
        assertDoesNotThrow(() -> {
            List<Character> chars = new LinkedList<Character>();
            chars.add('a');

            Validator validator = new Validator();
            validator.validate("string", "test")
                .min(3, "...");
            validator.validate("list", chars)
                .min(1, "...");
            validator.validate("array", chars.toArray())
                .min(1, "...");
            validator.validate("number", 3.2)
                .min(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("string", true)
                .min(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("string", "")
                .min(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            List<Character> chars = new LinkedList<Character>();

            Validator validator = new Validator();
            validator.validate("list", chars)
                .min(1, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            List<Character> chars = new LinkedList<Character>();

            Validator validator = new Validator();
            validator.validate("array", chars.toArray())
                .min(1, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("number", 2.2)
                .min(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("any", null)
                .min(0, "...");
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("any", null)
                .min(0, "...")
                .nullable();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("any", "")
                .min(0, "...")
                .nullable();
            validator.run();
        });
    };

    @Test
    @Order(3)
    @DisplayName("Max")
    public void mustValidateMax() {
        assertDoesNotThrow(() -> {
            List<Character> chars = new LinkedList<Character>();
            chars.add('a');

            Validator validator = new Validator();
            validator.validate("string", "test")
                .max(4, "...");
            validator.validate("list", chars)
                .max(1, "...");
            validator.validate("array", chars.toArray())
                .max(1, "...");
            validator.validate("number", 3.2)
                .max(4, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("string", true)
                .max(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("string", "test")
                .max(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            List<Character> chars = new LinkedList<Character>();
            chars.add('a');

            Validator validator = new Validator();
            validator.validate("list", chars)
                .max(0, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            List<Character> chars = new LinkedList<Character>();
            chars.add('a');

            Validator validator = new Validator();
            validator.validate("array", chars.toArray())
                .max(0, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("number", 4.2)
                .max(3, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("any", null)
                .max(0, "...");
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("any", null)
                .max(0, "...")
                .nullable();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("any", "")
                .max(0, "...")
                .nullable();
            validator.run();
        });
    };

    @Test
    @Order(4)
    @DisplayName("Email")
    public void mustValidateEmail() {
        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("email", "admin@gmail.com")
                .email("...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("email", "gmail.com")
                .email("...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("email", 0)
                .email("...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("email", null)
                .email("...");
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("email", null)
                .email("...")
                .nullable();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("email", "@")
                .email("...")
                .nullable();
            validator.run();
        });
    };

    @Test
    @Order(5)
    @DisplayName("Pattern")
    public void mustValidatePattern() {
        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", "abc")
                .pattern("[a-z]*", "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", 0)
                .pattern("[A-Z]*", "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", "abc")
                .pattern("[A-Z]*", "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", "A")
                .pattern("[A-Z", "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", null)
                .pattern("[A-Z]*", "...");
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", null)
                .pattern("[A-Z]*", "...")
                .nullable();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", "A")
                .pattern("[A-Z]*", "...")
                .nullable();
            validator.run();
        });
    };

    @Test
    @Order(6)
    @DisplayName("Verify")
    public void mustValidateVerify() {
        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", "")
                .verify(true, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", "")
                .verify(false, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", null)
                .verify(false, "...");
            validator.run();
        });

        assertThrows(ValidationsError.class, () -> {
            Validator validator = new Validator();
            validator.validate("pattern", null)
                .verify(true, "...");
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", null)
                .verify(false, "...")
                .nullable();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", null)
                .verify(true, "...")
                .nullable();
            validator.run();
        });

        assertDoesNotThrow(() -> {
            Validator validator = new Validator();
            validator.validate("pattern", "")
                .verify(true, "...")
                .nullable();
            validator.run();
        });
    };
};