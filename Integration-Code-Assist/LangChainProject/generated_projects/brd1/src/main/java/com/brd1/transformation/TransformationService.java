package com.brd1.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

@Service
public class TransformationService {

    private static final Logger logger = LoggerFactory.getLogger(TransformationService.class);

    public String xmlToJson(String xml) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readTree(xml);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = jsonMapper.writeValueAsString(node);
        logger.info("XML to JSON transformation: {}", jsonString);
        return jsonString;
    }

    public byte[] xmlToJsonPdf(String xml, String templatePath) throws Exception {
        String json = xmlToJson(xml);

        // Load the JasperReports template
        InputStream templateStream = getClass().getResourceAsStream(templatePath);
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

        // Create a JSON data source
        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(json.getBytes());
        JsonDataSource jsonDataSource = new JsonDataSource(jsonDataStream);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), jsonDataSource);

        // Export to PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}