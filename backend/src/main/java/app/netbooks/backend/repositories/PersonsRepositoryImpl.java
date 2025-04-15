package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Person;

@Repository
public class PersonsRepositoryImpl extends BaseRepository implements PersonsRepository {
    public PersonsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public void initialize() {
        try {
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Persons(\n" +
                "    name VARCHAR(120) NOT NULL,\n" +
                "    email VARCHAR(320) NOT NULL PRIMARY KEY,\n" +
                "    password VARCHAR(24) NOT NULL,\n" +
                "    access INTEGER DEFAULT 0\n" +
                ");"
            );

            statement.execute(
                "INSERT INTO Persons(email, password, name, access) \n" +
                "VALUES ('admin@gmail.com', 'admin', 'Admin', 1)\n" +
                "ON CONFLICT (email) DO NOTHING;"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        };
    };

    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<Person>();

        try {
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT name, email, access FROM Persons"
            );

            while(result.next()) {
                String email = result.getString("email");
                String name = result.getString("name");
                int access = result.getInt("access");
                Person person = new Person(email, null, name, access);
                persons.add(person);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return persons;
    };
};
