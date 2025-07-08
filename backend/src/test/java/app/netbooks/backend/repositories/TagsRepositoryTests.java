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
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.TagsRepository;

public abstract class TagsRepositoryTests extends BaseTests {
    @Autowired
    private TagsRepository repository;

    @BeforeAll
    public static void clear(@Autowired Database database) throws SQLException {
        BaseTests.clear(database, "tag");
    };

    @Test
    @Order(1)
    @DisplayName("Create")
    public void mustCreate() {
        assertDoesNotThrow(() -> {
            repository.create(
                new Tag("Romance")
            );
            repository.create(
                new Tag("Science fiction")
            );
        });
    };

    @Test
    @Order(2)
    @DisplayName("Find")
    public void mustFind() {
        assertDoesNotThrow( () -> {
            List<Tag> tags = repository.findAll();
            assertEquals(2, tags.size());

            Tag tag = tags.get(0);
            Optional<Tag> tagFound = repository.findByName(tag.getName());
            assertTrue(tagFound.isPresent());
        });
    };

    @Test
    @Order(3)
    @DisplayName("Delete")
    public void mustDelete() {
        assertDoesNotThrow( () -> {
            Optional<Tag> tag = repository.findByName("Romance");
            assertTrue(tag.isPresent());
            repository.deleteByName(tag.get().getName());

            tag = repository.findByName("Romance");
            assertTrue(tag.isEmpty());
        });
    };

    @Test
    @Order(4)
    @DisplayName("Exceptions")
    public void mustThrowExpections() throws SQLException {
        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new Tag("Science fiction")
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new Tag(null)
            );
        });

        Database database = mock(Database.class);
        
        doThrow(
            new SQLException("Erro simulado de conexão!")
        ).when(database)
        .query(any());

        doThrow(
            new SQLException("Erro simulado de conexão!")
        ).when(database)
        .execute(any());

        assertDoesNotThrow(() -> {
            TagsRepositoryImpl tagsRepositoryImpl = new TagsRepositoryImpl(database);
            tagsRepositoryImpl.findAll();
            tagsRepositoryImpl.findByName(null);
        });

        assertThrows(InternalServerError.class, () -> {
            TagsRepositoryImpl tagsRepositoryImpl = new TagsRepositoryImpl(database);
            tagsRepositoryImpl.deleteByName(null);
        });
    };
};
