package j.application.service;

import j.application.model.SomeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostgresService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SomeData> readDataFromPostgres() {
        // TODO: Implement database connection, query execution, and result mapping.
        System.out.println("Reading data from PostgreSQL");
        String sql = "SELECT id, name FROM some_table";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SomeData data = new SomeData();
            data.setId(rs.getLong("id"));
            data.setName(rs.getString("name"));
            return data;
        });
    }

    public void writeDataToPostgres(SomeData data) {
        // TODO: Implement writing data to PostgreSQL.
        System.out.println("Writing data to PostgreSQL: " + data.getName());
    }
}