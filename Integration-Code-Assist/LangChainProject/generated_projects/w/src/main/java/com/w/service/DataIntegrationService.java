package com.w.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.w.model.SomeData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;

@Service
public class DataIntegrationService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${external.api.url}")
    private String externalApiUrl;

    @Value("${fip.directory}")
    private String fipDirectory;

    public String readDataFromPostgres() {
        StringBuilder result = new StringBuilder();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM some_table"); // Replace with your table name
            while (resultSet.next()) {
                result.append(resultSet.getString("column1")).append(", "); // Replace with your column names
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error reading from PostgreSQL: " + e.getMessage();
        }
        return result.toString();
    }

    public String fetchDataFromExternalApi() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(externalApiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error fetching data from external API: " + e.getMessage();
        }
    }

    public String transformDataToJson(SomeData data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error transforming data to JSON: " + e.getMessage();
        }
    }

    public String readXmlFromFip() {
        try {
            File file = new File(fipDirectory + "/data.xml");
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading XML from FIP: " + e.getMessage();
        }
    }
}