package com.brd1.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostgresqlService {

    private static final Logger logger = LoggerFactory.getLogger(PostgresqlService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> readData(String query) {
        logger.info("Executing query: {}", query);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
        logger.info("Query results: {}", results);
        return results;
    }

    public void writeData(String sql) {
        logger.info("Executing SQL: {}", sql);
        jdbcTemplate.execute(sql);
    }
}