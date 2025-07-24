package com.example.newapplication.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FipService {

    private static final Logger logger = LoggerFactory.getLogger(FipService.class);

    @Value("${fip.directory}")
    private String fipDirectory;

    public String readXmlFileFromFip() throws IOException {
        File directory = new File(fipDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("FIP directory does not exist or is not a directory: " + fipDirectory);
        }

        // Assuming a single XML file in the directory for simplicity.  You might need to refine this logic.
        File[] files = directory.listFiles(file -> file.getName().toLowerCase().endsWith(".xml"));

        if (files == null || files.length == 0) {
            throw new IOException("No XML files found in FIP directory.");
        }

        File xmlFile = files[0]; // Take the first XML file found

        return FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8);
    }

    public void writeXmlFileToFip(String xmlData) throws IOException {
        File directory = new File(fipDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create FIP directory: " + fipDirectory);
        }

        // Generate a filename based on timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);
        String filename = "data_" + timestamp + ".xml";

        File xmlFile = new File(directory, filename);
        FileUtils.writeStringToFile(xmlFile, xmlData, StandardCharsets.UTF_8);

        logger.info("XML file written to FIP: " + xmlFile.getAbsolutePath());
    }
}