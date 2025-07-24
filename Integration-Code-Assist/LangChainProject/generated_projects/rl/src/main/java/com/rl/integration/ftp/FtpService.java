package com.rl.integration.ftp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;

@Service
@Slf4j
public class FtpService {

    public InputStream readFile(String filePath) {
        // TODO: Implement FTP file reading logic
        log.info("Reading file from FTP: {}", filePath);
        return null; // Replace with actual InputStream
    }

    public void writeFile(String filePath, OutputStream outputStream) {
        // TODO: Implement FTP file writing logic
        log.info("Writing file to FTP: {}", filePath);
    }
}