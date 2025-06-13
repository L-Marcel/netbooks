package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.repositories.interfaces.PlansRepository;

@Repository
public class PlansRepositoryImpl extends BaseRepository implements PlansRepository {
    public PlansRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Plan> findAllAvailable() {
        List<Plan> plans = new ArrayList<Plan>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
               "SELECT * FROM plan_with_availability WHERE available;"
            );
        ) {
            while(result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Integer numSubscribers = result.getInt("num_subscribers");
                Duration duration = Duration.ofHours(result.getLong("duration"));
                
                Plan plan = new Plan(
                    id, name, description, 
                    numSubscribers, duration
                );

                plans.add(plan);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return plans;
    };

    @Override
    public List<Plan> findAll() {
        List<Plan> plans = new ArrayList<Plan>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM plan_with_availability;"
            );
        ) {
            while(result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Integer numSubscribers = result.getInt("num_subscribers");
                Duration duration = Duration.ofHours(result.getLong("duration"));
                
                Plan plan = new Plan(
                    id, name, description, 
                    numSubscribers, duration
                );

                plans.add(plan);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return plans;
    };

    @Override
    public Optional<Plan> findById(Integer id) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM plan_with_availability WHERE id = ?;"
            );
        ){
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                Optional<Plan> planFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String description = result.getString("description");
                    Integer numSubscribers = result.getInt("num_subscribers");
                    Duration duration = Duration.ofHours(result.getLong("duration"));
                    
                    Plan plan = new Plan(
                        id, name, description, 
                        numSubscribers, duration
                    );
                    
                    planFound = Optional.of(plan);
                };
                
                return planFound;
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    };

    @Override
    public void create(Plan plan) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO plan (name, description, duration) VALUES (?, ?, ?);"
            );
        ) {
            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());
            statement.setLong(3, plan.getDuration().toHours());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };

    @Override
    public void update(Plan plan) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE plan\n" +
                "SET name = ?,\n" +
                "    description = ?,\n" +
                "    duration = ?\n" +
                "WHERE id = ?;"
            );
        ) {
            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());
            statement.setLong(3, plan.getDuration().toHours());
            statement.setInt(4, plan.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public Map<Integer, Integer> compareBenefitsById(Integer a, Integer b) {
        Map<Integer, Integer> counter = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "CALL compare_plans(?, ?)"
            );
        ){
            statement.setInt(1, a);
            statement.setInt(2, b);

            try(ResultSet result = statement.executeQuery()) {
                while(result.next()) {
                    Integer id = result.getInt("id");
                    Integer amount = result.getInt("amount");
                    
                    counter.put(id, amount);
                };
                
                return counter;
            }
        } catch (SQLException e) {
            return counter;
        }
    };
};
