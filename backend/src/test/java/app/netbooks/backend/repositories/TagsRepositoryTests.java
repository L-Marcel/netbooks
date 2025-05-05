package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.netbooks.backend.BaseTests;
import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Tag;

public abstract class TagsRepositoryTests extends BaseTests {
    @Autowired
    private TagsRepository repository;

    @BeforeAll
    public static void clear(@Autowired Database database) {
        BaseTests.clear(database, "Tags");
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

            List<Tag> tagsList = repository.findAll();
            assertEquals(1, tagsList.size());
        });
    };
};
