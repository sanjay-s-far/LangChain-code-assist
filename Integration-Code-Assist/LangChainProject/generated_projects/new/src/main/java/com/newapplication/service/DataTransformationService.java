package com.newapplication.service;

import com.newapplication.util.DataConverter;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;

@Service
public class DataTransformationService {

    public String xmlToJson(String xmlData) throws Exception {
        // TODO: Implement XML to JSON conversion logic.
        // Use Jackson XML module or similar.
        // Handle nested structures and schema mapping.
        return DataConverter.xmlToJson(xmlData);
    }

    public byte[] xmlToPdf(String xmlData) throws Exception {
        // TODO: Implement XML to PDF conversion.
        //  - Parse XML
        //  - Transform data for PDF template
        //  - Generate PDF using JasperReports or similar.
        //  - Return PDF as byte array.
        try {
            // Parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            // Extract data from XML (replace with your actual XML parsing logic)
            // Example: Extract a list of items from the XML
            // List<Item> items = extractItemsFromXml(document);
            // For this example, we'll create a dummy list:
            List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item1 = new HashMap<>();
            item1.put("name", "Product A");
            item1.put("price", 25.0);
            items.add(item1);

            Map<String, Object> item2 = new HashMap<>();
            item2.put("name", "Product B");
            item2.put("price", 40.0);
            items.add(item2);

            // Load JasperReports template (replace with your actual template path)
            // Make sure your jrxml file is in src/main/resources
            String templatePath = "src/main/resources/template.jrxml";  // Replace this with the path to your template file.
            JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);

            // Set parameters for the report (if any)
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Product List");

            // Create a data source from the list of items
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error generating PDF: " + e.getMessage());
        }

    }
}