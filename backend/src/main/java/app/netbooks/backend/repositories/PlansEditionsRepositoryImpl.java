package app.netbooks.backend.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.repositories.interfaces.PlansEditionsRepository;

@Repository
public class PlansEditionsRepositoryImpl extends BaseRepository implements PlansEditionsRepository {
    public PlansEditionsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Integer, List<PlanEdition>> mapAllAvailableByPlan() {
        return this.queryOrDefault((connection) -> {
            Map<Integer, List<PlanEdition>> editionsMap = new LinkedHashMap<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    "SELECT * FROM plan_edition_with_subscribers\n" +
                    "WHERE available\n" +
                    "ORDER BY price ASC;"
                );
            ) {
                while(result.next()) {
                    Integer id = result.getInt("id");
                    Integer plan = result.getInt("plan");
                    Integer numSubscribers = result.getInt("num_subscribers");
                    BigDecimal price = result.getBigDecimal("price");
                    Date startedIn = result.getDate("started_in");
                    Date closedIn = result.getDate("closed_in");
                    Boolean available = result.getBoolean("available");
                
                    PlanEdition plan_edition = new PlanEdition(
                        id, plan, numSubscribers, price, 
                        startedIn, closedIn, available
                    );

                    editionsMap.computeIfAbsent(
                        plan,
                        v -> new ArrayList<PlanEdition>()
                    ).add(plan_edition);
                };
            };

            return editionsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public Map<Integer, List<PlanEdition>> mapAllByPlan() {
        return this.queryOrDefault((connection) -> {
            Map<Integer, List<PlanEdition>> editionsMap = new LinkedHashMap<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    "SELECT * FROM plan_edition_with_subscribers\n" +
                    "ORDER BY available DESC, price ASC;"
                );
            ) {
                while(result.next()) {
                    Integer id = result.getInt("id");
                    Integer plan = result.getInt("plan");
                    Integer numSubscribers = result.getInt("num_subscribers");
                    BigDecimal price = result.getBigDecimal("price");
                    Date startedIn = result.getDate("started_in");
                    Date closedIn = result.getDate("closed_in");
                    Boolean available = result.getBoolean("available");
                
                    PlanEdition plan_edition = new PlanEdition(
                        id, plan, numSubscribers, price, 
                        startedIn, closedIn, available
                    );

                    editionsMap.computeIfAbsent(
                        plan,
                        v -> new ArrayList<PlanEdition>()
                    ).add(plan_edition);
                };
            };

            return editionsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public List<PlanEdition> findAll() {
        return this.queryOrDefault((connection) -> {
            List<PlanEdition> editions = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    "SELECT * FROM plan_edition_with_subscribers\n" +
                    "ORDER BY available DESC, price ASC;"
                );
            ) {
                while(result.next()) {
                    Integer id = result.getInt("id");
                    Integer plan = result.getInt("plan");
                    Integer numSubscribers = result.getInt("num_subscribers");
                    BigDecimal price = result.getBigDecimal("price");
                    Date startedIn = result.getDate("started_in");
                    Date closedIn = result.getDate("closed_in");
                    Boolean available = result.getBoolean("available");
                
                    PlanEdition plan_edition = new PlanEdition(
                        id, plan, numSubscribers, price, 
                        startedIn, closedIn, available
                    );

                    editions.add(plan_edition);
                };
            };

            return editions;
        }, new ArrayList<>());
    };

    @Override
    public Optional<PlanEdition> findById(Integer id) {
        return this.queryOrDefault((connection) -> {
            Optional<PlanEdition> planEditionFoumd = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM plan_edition_with_subscribers\n" +
                    "WHERE id = ?;"
                );
            ) {
                statement.setInt(1, id);

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Integer plan = result.getInt("plan");
                        Integer numSubscribers = result.getInt("num_subscribers");
                        BigDecimal price = result.getBigDecimal("price");
                        Date startedIn = result.getDate("started_in");
                        Date closedIn = result.getDate("closed_in");
                        Boolean available = result.getBoolean("available");

                        PlanEdition plan_edition = new PlanEdition(
                            id, plan, numSubscribers, price, 
                            startedIn, closedIn, available
                        );

                        planEditionFoumd = Optional.of(plan_edition);
                    };

                    
                }
            };

            return planEditionFoumd;
        }, Optional.empty());
    };
};
