package com.ff.service;

import com.ff.util.XmlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataTransformationService {

    private final XmlUtil xmlUtil;

    public String convertXmlToJson(String xmlData) throws Exception {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xmlData);

            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(node);

        } catch (Exception e) {
            log.error("Error converting XML to JSON: " + e.getMessage(), e);
            throw e;
        }
    }

    public byte[] convertXmlToJsonAndPdf(String xmlData) throws Exception {
        // Implement the XML to JSON to PDF conversion logic here
        // This will involve using JasperReports for PDF generation
        return null; // Replace with the generated PDF byte array
    }
}