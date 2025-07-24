package com.example.newapplication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class DataTransformationService {

    private static final Logger logger = LoggerFactory.getLogger(DataTransformationService.class);

    public String convertXmlToJson(String xmlData) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xmlData.getBytes());

            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(node);

        } catch (Exception e) {
            logger.error("Error converting XML to JSON: ", e);
            return "Error: " + e.getMessage();
        }
    }

    public byte[] convertXmlToJsonAndPdf(String xmlData) {
        try {
            // 1. Convert XML to JSON
            String jsonData = convertXmlToJson(xmlData);

            // 2. Generate PDF from JSON
            return generatePdfFromJson(jsonData);

        } catch (Exception e) {
            logger.error("Error converting XML to JSON and PDF: ", e);
            return ("Error: " + e.getMessage()).getBytes();
        }
    }

    private byte[] generatePdfFromJson(String jsonData) {
        try {
            // Load the JasperReports template (replace with your actual template)
            InputStream templateStream = this.getClass().getResourceAsStream("/report_template.jrxml"); // Place your jrxml file in the resources folder
            if (templateStream == null) {
                throw new RuntimeException("Report template not found!");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

            // Create a JSON data source
            JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8)));

            // Set parameters (if any)
            //Map<String, Object> parameters = new HashMap<>();
            //parameters.put("parameterName", "parameterValue");

            // Generate the PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jsonDataSource);

            // Export to PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating PDF from JSON: ", e);
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }
}