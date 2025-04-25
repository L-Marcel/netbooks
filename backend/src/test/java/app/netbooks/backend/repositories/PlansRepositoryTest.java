package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import app.netbooks.backend.BaseTest;
import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Plan;

public class PlansRepositoryTest extends BaseTest {
    @Autowired
    public PlansRepository repository;

    @BeforeAll
    public static void clear(@Autowired Database database) {
        BaseTest.clear(database, "Plans");
    };

    @Test
    @Order(1)
    public void shouldCreate() {
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
    public void shouldFind() {
        assertDoesNotThrow(() -> {
            List<Plan> plans = repository.findAll();
            assertEquals(1, plans.size());
            Plan plan = plans.get(0);
            Optional<Plan> planFound = repository.findById(plan.getUuid());
            assertTrue(planFound.isPresent());
        });
    };
};
