package com.example.pub.service;

import com.example.pub.integration.api.ApiService;
import com.example.pub.integration.fip.FipService;
import com.example.pub.integration.pubsub.PubSubService;
import com.example.pub.model.DataModel;
import com.example.pub.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.XML;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class DataService {

    private final PubSubService pubSubService;
    private final DataRepository dataRepository;
    private final ApiService apiService;
    private final FipService fipService;

    @Autowired
    public DataService(PubSubService pubSubService, DataRepository dataRepository, ApiService apiService, FipService fipService) {
        this.pubSubService = pubSubService;
        this.dataRepository = dataRepository;
        this.apiService = apiService;
        this.fipService = fipService;
    }

    public String getData() {
        // Example: Fetch data from PostgreSQL
        DataModel dataModel = dataRepository.findById(1L).orElse(new DataModel());
        return dataModel.getData();
    }

    public void processData(String data) {
        // Example: Send a message to Pub/Sub
        pubSubService.sendMessage("my-topic", data);
    }

    public String readDataFromApi(String apiUrl, String username, String password) {
        return apiService.fetchDataFromExternalApi(apiUrl, username, password);
    }

    public String readXmlFromFip() {
        return fipService.readXmlFromFip();
    }

    public void writeXmlToFip(String xmlData) {
        fipService.writeXmlToFip(xmlData);
    }

    public String convertXmlToJson(String xml) {
        try {
            JSONObject json = XML.toJSONObject(xml);
            return json.toString(4); // Indent with 4 spaces
        } catch (Exception e) {
            return "Error converting XML to JSON: " + e.getMessage();
        }
    }

    public String convertXmlToJsonAndPdf(String xml) {
        String json = convertXmlToJson(xml);
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
            document.open();
            document.add(new Paragraph(json));
            document.close();
            return "Successfully converted XML to JSON and PDF.";
        } catch (Exception e) {
            return "Error converting XML to JSON and PDF: " + e.getMessage();
        }
    }
}