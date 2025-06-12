package app.netbooks.backend.repositories;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.repositories.interfaces.PlansEditionsRepository;

@Repository
public class PlansEditionsRepositoryImpl extends BaseRepository implements PlansEditionsRepository {
    public PlansEditionsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Integer, List<PlanEdition>> mapAllAvailableByPlan() {
        Map<Integer, List<PlanEdition>> editionsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM plan_edition_with_subscribers\n" +
                "WHERE closed_in IS NULL\n" +
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
            
                PlanEdition plan_edition = new PlanEdition(
                    id, plan, numSubscribers, price, 
                    startedIn, closedIn
                );

                editionsMap.computeIfAbsent(
                    plan,
                    v -> new ArrayList<PlanEdition>()
                ).add(plan_edition);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return editionsMap;
    };

    @Override
    public Map<Integer, List<PlanEdition>> mapAllByPlan() {
        Map<Integer, List<PlanEdition>> editionsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM plan_edition_with_subscribers\n" +
                "ORDER BY closed_in IS NULL DESC, price ASC;"
            );
        ) {
            while(result.next()) {
                Integer id = result.getInt("id");
                Integer plan = result.getInt("plan");
                Integer numSubscribers = result.getInt("num_subscribers");
                BigDecimal price = result.getBigDecimal("price");
                Date startedIn = result.getDate("started_in");
                Date closedIn = result.getDate("closed_in");
            
                PlanEdition plan_edition = new PlanEdition(
                    id, plan, numSubscribers, price, 
                    startedIn, closedIn
                );

                editionsMap.computeIfAbsent(
                    plan,
                    v -> new ArrayList<PlanEdition>()
                ).add(plan_edition);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return editionsMap;
    };

    @Override
    public List<PlanEdition> findAll() {
        List<PlanEdition> editions = new ArrayList<PlanEdition>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM plan_edition_with_subscribers\n" +
                "ORDER BY closed_in IS NULL DESC, price ASC;"
            );
        ) {
            while(result.next()) {
                Integer id = result.getInt("id");
                Integer plan = result.getInt("plan");
                Integer numSubscribers = result.getInt("num_subscribers");
                BigDecimal price = result.getBigDecimal("price");
                Date startedIn = result.getDate("started_in");
                Date closedIn = result.getDate("closed_in");
            
                PlanEdition plan_edition = new PlanEdition(
                    id, plan, numSubscribers, price, 
                    startedIn, closedIn
                );

                editions.add(plan_edition);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return editions;
    };
};
