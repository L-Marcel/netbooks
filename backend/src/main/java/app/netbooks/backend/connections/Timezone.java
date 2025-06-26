package app.netbooks.backend.connections;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;

import app.netbooks.backend.connections.interfaces.Database;

public class Timezone {
    @Autowired
    private Database database;

    public ZoneId getZoneId() {
        try {
            return this.database.query((connection) -> {
                ZoneId zoneId = ZoneId.systemDefault();

                try(
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(
                        // language=sql
                        """
                        SELECT @@time_zone AS time_zone;
                        """
                    );
                ) {
                    if(result.next()) {
                        String offset = result.getString("time_zone");
                        zoneId = ZoneId.of(offset);
                    };
                };

                return zoneId;
            });
        } catch (Exception e) {
            return ZoneId.systemDefault();
        }
    };
};
