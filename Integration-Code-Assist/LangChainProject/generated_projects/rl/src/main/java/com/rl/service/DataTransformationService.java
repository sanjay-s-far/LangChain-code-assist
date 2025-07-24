package com.rl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class DataTransformationService {

    public JsonNode convertXmlToJson(InputStream xmlInputStream) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readTree(xmlInputStream);
        return node;
    }

    public byte[] convertXmlToJsonAndPdf(InputStream xmlInputStream) throws IOException {
        JsonNode jsonNode = convertXmlToJson(xmlInputStream);
        String jsonString = new ObjectMapper().writeValueAsString(jsonNode);

        // Generate PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph(jsonString)); // Add JSON content to PDF
            document.close();
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage());
            throw new IOException("Error generating PDF", e);
        }

        return outputStream.toByteArray();
    }
}