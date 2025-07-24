package com.brd1.controller;

import com.brd1.api.ApiService;
import com.brd1.fip.FipService;
import com.brd1.postgresql.PostgresqlService;
import com.brd1.pubsub.PubSubService;
import com.brd1.transformation.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private PubSubService pubSubService;

    @Autowired
    private PostgresqlService postgresqlService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private FipService fipService;

    @Autowired
    private TransformationService transformationService;

    @GetMapping("/pubsub/publish")
    public String publishMessage(@RequestParam String topic, @RequestParam String message) {
        pubSubService.publishMessage(topic, message);
        return "Message published to Pub/Sub";
    }

    @GetMapping("/postgres/read")
    public List<Map<String, Object>> readPostgresData(@RequestParam String query) {
        return postgresqlService.readData(query);
    }

    @GetMapping("/api/fetch")
    public String fetchDataFromApi(@RequestParam String url) throws IOException {
        return apiService.fetchDataFromApi(url);
    }

    @GetMapping("/fip/read")
    public String readFipFile(@RequestParam String filePath) {
        return fipService.readXmlFile(filePath);
    }

    @PostMapping("/fip/write")
    public String writeFipFile(@RequestParam String filePath, @RequestBody String xmlContent) {
        fipService.writeXmlFile(filePath, xmlContent);
        return "XML written to FIP";
    }

    @PostMapping("/transform/xmltojson")
    public String transformXmlToJson(@RequestBody String xml) throws Exception {
        return transformationService.xmlToJson(xml);
    }

    @PostMapping("/transform/xmltopdf")
    public ResponseEntity<byte[]> transformXmlToPdf(@RequestBody String xml, @RequestParam String templatePath) throws Exception {
        byte[] pdfBytes = transformationService.xmlToJsonPdf(xml, templatePath);
        return new ResponseEntity<>(pdfBytes, HttpStatus.OK);
    }
}