package app.netbooks.backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Access;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.PlansRepository;
import app.netbooks.backend.repositories.TagsRepository;
import app.netbooks.backend.repositories.UsersRepository;

@SpringBootTest
@DisplayName("Repositories")
public class RepositoriesTestSuite extends BaseTest {
    @Nested
    @Order(1)
    @DisplayName("Plans")
    class PlansRepositoryTests {
        @Autowired
        public PlansRepository repository;

        @BeforeAll
        public static void clear(@Autowired Database database) {
            BaseTest.clear(database, "Plans");
        };

        @Test
        @Order(1)
        @DisplayName("Create")
        public void mustCreate() {
            assertDoesNotThrow(() -> {
                repository.create(
                    new Plan(
                        "Plano Teste", 
                        "Apenas para testes...",
                        Duration.ofDays(30)
                    )
                );
            });
        };

        @Test
        @Order(2)
        @DisplayName("Find")
        public void mustFind() {
            assertDoesNotThrow(() -> {
                List<Plan> plans = repository.findAll();
                assertEquals(1, plans.size());
                Plan plan = plans.get(0);
                Optional<Plan> planFound = repository.findById(plan.getUuid());
                assertTrue(planFound.isPresent());
            });
        };

        @Test
        @Order(3)
        @DisplayName("Update")
        public void mustUpdate() {
            assertDoesNotThrow(() -> {
                List<Plan> plans = repository.findAll();
                Plan plan = plans.get(0);
                plan.setName("Plano");
                repository.update(plan);
                Optional<Plan> planFound = repository.findById(plan.getUuid());
                assertEquals("Plano", planFound.get().getName());
                plan.setDuration(Duration.ofSeconds(30));
                repository.update(plan);
                planFound = repository.findById(plan.getUuid());
                assertEquals(30, planFound.get().getDuration().toSeconds());
            });
        };
    };

    @Nested
    @Order(4)
    @DisplayName("Users")
    class UsersRepositoryTests {
        @Autowired
        public UsersRepository repository;

        @Test
        @Order(1)
        @DisplayName("Have administrator")
        public void mustHaveAdministrator() {
            assertDoesNotThrow(() -> {
                List<User> users = repository.findAll();
                assertEquals(1, users.size());
                User user = users.get(0);
                assertEquals(Access.ADMINISTRATOR, user.getAccess());
            });
        };
    };
    
    @Nested
    @Order(5)
    @DisplayName("Tags")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TagsRepositoryTests {
        @Autowired
        private TagsRepository repository;

        @BeforeAll
        public static void clear(@Autowired Database database) {
            BaseTest.clear(database, "Tags");
        };

        @Test
        @Order(1)
        @DisplayName("Create Tag")
        public void createTag() {
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
        @DisplayName("Find All Tags")
        public void findAllTags() {
            assertDoesNotThrow( () -> {
                List<Tag> tags = repository.findAll();
                assertEquals(2, tags.size());
            });
        };


        @Test
        @Order(3)
        @DisplayName("Find By Id Tag")
        public void findByIdTag() {
            assertDoesNotThrow( () -> {
                Tag tag = repository.findById("Romance");
                assertNotNull(tag);
                assertEquals("Romance", tag.getName());
            });
        };

        @Test
        @Order(4)
        @DisplayName("Delete Tags")
        public void deleteTags() {
            assertDoesNotThrow( () -> {
                Tag tag = repository.findById("Romance");
                assertNotNull(tag);
                repository.delete(tag);

                List<Tag> tagsList = repository.findAll();
                assertEquals(1, tagsList.size());
            });
        };

    };
};
