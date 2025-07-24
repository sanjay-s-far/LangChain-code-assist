package j.application.controller;

import j.application.model.SomeData;
import j.application.service.ApiService;
import j.application.service.PostgresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    private final PostgresService postgresService;
    private final ApiService apiService;

    @Autowired
    public DataController(PostgresService postgresService, ApiService apiService) {
        this.postgresService = postgresService;
        this.apiService = apiService;
    }

    @GetMapping("/postgres")
    public ResponseEntity<List<SomeData>> getDataFromPostgres() {
        try {
            List<SomeData> data = postgresService.readDataFromPostgres();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/external-api")
    public ResponseEntity<String> getDataFromExternalApi() {
        try {
            String data = apiService.fetchDataFromExternalApi();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/secured")
    public String securedEndpoint() {
        return "This is a secured endpoint!";
    }
}