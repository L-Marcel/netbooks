package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.sql.SQLException;
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
import app.netbooks.backend.models.Author;
import app.netbooks.backend.repositories.interfaces.AuthorsRepository;

public class AuthorRepositoryTests extends BaseTests {
    @Autowired
    private AuthorsRepository repository;

    private Author testAuthor;

    @BeforeAll
    public static void clear(@Autowired Database database) throws SQLException {
        BaseTests.clear(database, "author");
    }

    @Test
    @Order(1)
    @DisplayName("Create")
    public void mustCreate() {
        assertDoesNotThrow(() -> {
            repository.create(
                new Author("Victoria Aveyard")
            );
            repository.create(
                new Author("Tori Christin")
            );
        });
    }
    
    @Test
    @Order(2)
    @DisplayName("Search")
    public void mustSearch() {
        assertDoesNotThrow(() -> {
            List<Author> authors = repository.searchByName("tori");
            assertEquals(2, authors.size());
        });
    }

    @Test
    @Order(3)
    @DisplayName("Find")
    public void mustFind() {
        assertDoesNotThrow(() -> {
            List<Author> authors = repository.searchByName("Victoria Aveyard");
            assertEquals(1, authors.size());
            this.testAuthor = authors.getFirst();
            Optional<Author> author = repository.findById(this.testAuthor.getId());
            assertEquals(this.testAuthor.getName(), author.get().getName());
        });
    }

    @Test
    @Order(4)
    @DisplayName("Delete")
    public void mustDelete() {
        assertDoesNotThrow(() -> {
            repository.deleteById(this.testAuthor.getId());
            Optional<Author> author = repository.findById(this.testAuthor.getId());
            assertTrue(author.isEmpty());
        });
    }

    @Test
    @Order(5)
    @DisplayName("Exceptions")
    public void mustThrowExpections() throws SQLException {
        Database database = mock(Database.class);
        
        doThrow(
            new SQLException("Erro simulado de conexão!")
        ).when(database)
        .query(any());

        doThrow(
            new SQLException("Erro simulado de conexão!")
        ).when(database)
        .execute(any());

        assertThrows(InternalServerError.class, () -> {
            UsersRepositoryImpl usersRepositoryImpl = new UsersRepositoryImpl(database);
            usersRepositoryImpl.deleteById(null);
        });
    }
}
