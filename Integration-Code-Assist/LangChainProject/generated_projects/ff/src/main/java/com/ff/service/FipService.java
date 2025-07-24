package com.ff.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FipService {

    @Value("${fip.directory}")
    private String fipDirectory;

    @Value("${fip.xml.filename}")
    private String fipXmlFilename;

    public String readXmlFromFip() throws IOException {
        Path filePath = Paths.get(fipDirectory, fipXmlFilename);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            log.error("Error reading XML file from FIP: " + e.getMessage(), e);
            throw e;
        }
    }

    public void writeXmlToFip(String xmlData) throws IOException {
        Path filePath = Paths.get(fipDirectory, fipXmlFilename);
        try {
            Files.writeString(filePath, xmlData);
            log.info("Successfully wrote XML to FIP: " + filePath);
        } catch (IOException e) {
            log.error("Error writing XML file to FIP: " + e.getMessage(), e);
            throw e;
        }
    }
}