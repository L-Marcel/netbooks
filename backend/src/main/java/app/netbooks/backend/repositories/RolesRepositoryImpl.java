package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Role;
import app.netbooks.backend.repositories.interfaces.RolesRepository;

@Repository
public class RolesRepositoryImpl extends BaseRepository implements RolesRepository {
    public RolesRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<UUID, List<Role>> mapAllByUser() {
        Map<UUID, List<Role>> rolesMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM user_roles;"
            );
        ) {
            while(result.next()) {
                UUID user = UUID.fromString(result.getString("uuid"));
                Role role = Role.fromString(result.getString("role"));
                
                rolesMap.computeIfAbsent(
                    user,
                    v -> new ArrayList<Role>()
                ).add(role);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return rolesMap;
    };

    @Override
    public List<Role> findAllByUser(UUID user) {
        List<Role> roles = new LinkedList<>();

        try (
            Connection connection = this.database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT role FROM user_roles WHERE uuid = ?;"
            );
        ) {
            preparedStatement.setString(1, user.toString());
            
            try (
                ResultSet result = preparedStatement.executeQuery();
            ) {
                while(result.next()) {
                    Role role = Role.fromString(result.getString("role"));
                    roles.add(role);
                };
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return roles;
    };
};
