package com.example.j.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class DataConverter {

    public String convertXmlToJson(String xml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xml);

            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(node);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String convertXmlToPdf(String xml) {
      // TODO: Implement XML to PDF conversion using PDFBox or other suitable library
      return "PDF Conversion not yet implemented";
    }

    // Example of converting XML to a Document object
    public Document convertXmlToDocument(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Example of converting Document object to XML String
    public String convertDocumentToXml(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}