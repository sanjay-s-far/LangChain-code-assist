package j.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class XmlJsonService {

    public JsonNode convertXmlToJson(String xml) throws IOException {
        // TODO: Implement XML to JSON conversion using Jackson XML.
        System.out.println("Converting XML to JSON: " + xml);
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readTree(xml);
        return node;
    }
}