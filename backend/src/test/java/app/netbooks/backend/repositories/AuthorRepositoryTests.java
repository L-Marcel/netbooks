package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
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
import app.netbooks.backend.models.Author;

public class AuthorRepositoryTests extends BaseTests {
    @Autowired
    private AuthorRepository repository;

    @BeforeAll
    public static void clear(@Autowired Database database) {
        BaseTests.clear(database, "author");
    }

    @Test
    @Order(1)
    @DisplayName("Create")
    public void mustCreate() {
        assertDoesNotThrow(() -> {
            repository.create(
                new Author(1, "Victoria Aveyard")
            );
            repository.create(
                new Author(2, "Tori Christin")
            );
        });
    }
    
    @Test
    @Order(2)
    @DisplayName("Find")
    public void mustFind() {
        assertDoesNotThrow(() -> {
            Optional<Author> author = repository.findById(1);
            assertEquals("Victoria Aveyard", author.get().getName());
        });
    }
    
    @Test
    @Order(3)
    @DisplayName("Search")
    public void mustSearch() {
        assertDoesNotThrow(() -> {
            List<Author> authors = repository.searchByName("tori");
            assertEquals(2, authors.size());
        });
    }

    @Test
    @Order(4)
    @DisplayName("Delete")
    public void mustDelete() {
        assertDoesNotThrow(() -> {
            Optional<Author> author = repository.findById(1);
            assertTrue(author.isPresent());
            repository.deleteById(1);
            
            author = repository.findById(1);
            assertTrue(author.isEmpty());
        });
    }

    @Test
    @Order(5)
    @DisplayName("Exceptions")
    public void mustThrowExpections() throws SQLException {
        assertThrows(InternalServerError.class, () -> {
            repository.deleteById(1);
        });
    }
}
