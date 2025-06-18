package app.netbooks.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.Duration;
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
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.repositories.interfaces.PlansRepository;

public abstract class PlansRepositoryTests extends BaseTests {
    @Autowired
    public PlansRepository repository;

    @BeforeAll
    public static void clear(@Autowired Database database) {
        BaseTests.clear(database, "plan");
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
            Optional<Plan> planFound = repository.findById(plan.getId());
            assertTrue(planFound.isPresent());

            planFound = repository.findById(null);
            assertTrue(planFound.isEmpty());
        });
    };

    @Test
    @Order(3)
    @DisplayName("Update")
    public void mustUpdate() {
        assertDoesNotThrow(() -> {
            List<Plan> plans = repository.findAll();
            assertEquals(1, plans.size());

            Plan plan = plans.get(0);
            plan.setName("Plano");
            repository.update(plan);

            Optional<Plan> planFound = repository.findById(plan.getId());
            assertEquals("Plano", planFound.get().getName());
            
            plan.setDuration(Duration.ofHours(30));
            repository.update(plan);
            planFound = repository.findById(plan.getId());
            assertEquals(30, planFound.get().getDuration().toHours());
        });
    };

    @Test
    @Order(4)
    @DisplayName("Exceptions")
    public void mustThrowExpections() throws SQLException {
        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new Plan(
                    "Plano", 
                    "Apenas para testes...",
                    Duration.ofDays(30)
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new Plan(
                    null, 
                    "Apenas para testes...",
                    Duration.ofDays(30)
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            repository.create(
                new Plan(
                    "Plano Teste", 
                    null,
                    Duration.ofDays(30)
                )
            );
        });

        assertThrows(InternalServerError.class, () -> {
            List<Plan> plans = repository.findAll();
            assertEquals(1, plans.size());

            Plan plan = plans.get(0);
            plan.setName(null);
            repository.update(plan);
        });

        assertThrows(InternalServerError.class, () -> {
            List<Plan> plans = repository.findAll();
            assertEquals(1, plans.size());

            Plan plan = plans.get(0);
            plan.setDescription(null);
            repository.update(plan);
        });

        Database database = mock(Database.class);
        when(database.getConnection()).thenThrow(
            new SQLException("Erro simulado de conexão!")
        );

        assertDoesNotThrow(() -> {
            PlansRepositoryImpl plansRepository = new PlansRepositoryImpl(database);
            plansRepository.initialize();
            plansRepository.findAll();
            plansRepository.findById(null);
        });
    };
};
