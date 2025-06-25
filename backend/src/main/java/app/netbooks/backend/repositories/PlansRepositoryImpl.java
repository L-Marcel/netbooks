package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.repositories.interfaces.PlansRepository;

@Repository
public class PlansRepositoryImpl extends BaseRepository implements PlansRepository {
    public PlansRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Plan> findAllAvailable() {
        return this.queryOrDefault((connection) -> {
            List<Plan> plans = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM plan_with_availability 
                    WHERE available;
                    """
                );
            ) {
                while(result.next()) {
                    Integer id = result.getInt("id");
                    String name = result.getString("name");
                    String description = result.getString("description");
                    Integer numSubscribers = result.getInt("num_subscribers");
                    Duration duration = Duration.ofDays(result.getLong("duration"));
                    Boolean available = result.getBoolean("available");

                    Plan plan = new Plan(
                        id, name, description, 
                        numSubscribers, duration,
                        available
                    );

                    plans.add(plan);
                };
            };

            return plans;
        }, new ArrayList<>());
    };

    @Override
    public List<Plan> findAll() {
        return this.queryOrDefault((connection) -> {
            List<Plan> plans = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM plan_with_availability;
                    """
                );
            ) {
                while(result.next()) {
                    Integer id = result.getInt("id");
                    String name = result.getString("name");
                    String description = result.getString("description");
                    Integer numSubscribers = result.getInt("num_subscribers");
                    Duration duration = Duration.ofDays(result.getLong("duration"));
                    Boolean available = result.getBoolean("available");

                    Plan plan = new Plan(
                        id, name, description, 
                        numSubscribers, duration,
                        available
                    );

                    plans.add(plan);
                };
            };

            return plans;
        }, new ArrayList<>());
    };

    @Override
    public Optional<Plan> findById(Integer id) {
        return this.queryOrDefault((connection) -> {
            Optional<Plan> planFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM plan_with_availability 
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setInt(1, id);
                try(ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        String name = result.getString("name");
                        String description = result.getString("description");
                        Integer numSubscribers = result.getInt("num_subscribers");
                        Duration duration = Duration.ofDays(result.getLong("duration"));
                        Boolean available = result.getBoolean("available");

                        Plan plan = new Plan(
                            id, name, description, 
                            numSubscribers, duration,
                            available
                        );
                        
                        planFound = Optional.of(plan);
                    };
                };
            };

            return planFound;
        }, Optional.empty());
    };

    @Override
    public void create(Plan plan) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO plan (name, description, duration) 
                    VALUES (?, ?, ?);
                    """
                );
            ) {
                statement.setString(1, plan.getName());
                statement.setString(2, plan.getDescription());
                statement.setLong(3, plan.getDuration().toDays());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void update(Plan plan) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE plan
                    SET name = ?,
                        description = ?,
                        duration = ?
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setString(1, plan.getName());
                statement.setString(2, plan.getDescription());
                statement.setLong(3, plan.getDuration().toDays());
                statement.setInt(4, plan.getId());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public Map<Integer, Integer> compareBenefitsById(Integer a, Integer b) {
        return this.queryOrDefault((connection) -> {
            Map<Integer, Integer> counter = new LinkedHashMap<>();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT pln.id, COUNT(DISTINCT benefit) as amount
                    FROM plan AS pln
                    LEFT JOIN plan_benefits AS bnf
                    ON pln.id = bnf.plan
                    WHERE pln.id IN (?, ?)
                    GROUP BY pln.id;
                    """
                );
            ) {
                statement.setInt(1, a);
                statement.setInt(2, b);

                try(ResultSet result = statement.executeQuery()) {
                    while(result.next()) {
                        Integer id = result.getInt("id");
                        Integer amount = result.getInt("amount");
                        
                        counter.put(id, amount);
                    };
                };
            };
            
            return counter;
        }, new LinkedHashMap<>());
    };
};
