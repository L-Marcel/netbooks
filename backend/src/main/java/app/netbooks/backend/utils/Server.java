package app.netbooks.backend.utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.netbooks.backend.connections.interfaces.Database;

@Component
public class Server {
    @Autowired
    private Database database;

    public ZoneId getServerZoneId() {
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

    public Date getCurrentZoneDate(ZoneId zoneId) {
        return Date.valueOf(
            LocalDate.now(zoneId)
        );
    };
    
    public Date getServerCurrentDate() {
        return this.getCurrentZoneDate(
            this.getServerZoneId()
        );
    };
};
