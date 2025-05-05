package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Plan;

@Repository
public class PlansRepositoryImpl extends BaseRepository implements PlansRepository {
    public PlansRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public void initialize() {
        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
        ) {
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Plans(\n" +
                "    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),\n" +
                "    name VARCHAR(80) NOT NULL UNIQUE,\n" +
                "    description VARCHAR(400) NOT NULL,\n" +
                "    duration BIGINT DEFAULT 0\n" +
                ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };

    @Override
    public List<Plan> findAll() {
        List<Plan> plans = new ArrayList<Plan>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Plans;");
        ) {
            while(result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String name = result.getString("name");
                String description = result.getString("description");
                Duration duration = Duration.ofSeconds(result.getLong("duration"));
                Plan plan = new Plan(uuid, name, description, duration);
                plans.add(plan);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return plans;
    };

    @Override
    public Optional<Plan> findById(UUID uuid) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Plans WHERE uuid = ?;"
            );
        ){
            statement.setObject(1, uuid);
            try(ResultSet result = statement.executeQuery()) {
                Optional<Plan> planFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String description = result.getString("description");
                    Duration duration = Duration.ofSeconds(result.getLong("duration"));
                    Plan plan = new Plan(uuid, name, description, duration);
                    planFound = Optional.of(plan);
                };
                
                return planFound;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    };

    @Override
    public void create(Plan plan) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Plans (name, description, duration) VALUES (?, ?, ?);"
            );
        ) {
            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());
            statement.setLong(3, plan.getDuration().toSeconds());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void update(Plan plan) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE Plans SET (name, description, duration) = (?, ?, ?) WHERE uuid = ?;"
            );
        ) {
            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());
            statement.setLong(3, plan.getDuration().toSeconds());
            statement.setObject(4, plan.getUuid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };
};
