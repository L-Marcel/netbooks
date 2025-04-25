package app.netbooks.backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Access;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.PlansRepository;
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
};
