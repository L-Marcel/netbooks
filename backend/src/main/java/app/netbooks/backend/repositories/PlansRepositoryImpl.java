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

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Plan;

@Repository
public class PlansRepositoryImpl extends BaseRepository implements PlansRepository {
    public PlansRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Plan> findAll() {
        List<Plan> plans = new ArrayList<Plan>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM plan;");
        ) {
            while(result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Duration duration = Duration.ofSeconds(result.getLong("duration"));
                Plan plan = new Plan(id, name, description, duration);
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
                "SELECT * FROM plan WHERE id = ?;"
            );
        ){
            statement.setObject(1, id);
            try(ResultSet result = statement.executeQuery()) {
                Optional<Plan> planFound = Optional.empty();

                if(result.next()) {
                    String name = result.getString("name");
                    String description = result.getString("description");
                    Duration duration = Duration.ofSeconds(result.getLong("duration"));
                    Plan plan = new Plan(id, name, description, duration);
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
            statement.setLong(3, plan.getDuration().toSeconds());
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
            statement.setLong(3, plan.getDuration().toSeconds());
            statement.setInt(4, plan.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };
};
