package j.application.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generatePdf(String data, String filePath) throws IOException {
        // TODO: Implement PDF generation from data, potentially using a template.
        System.out.println("Generating PDF: " + filePath + " with data: " + data);
        // Example using Thymeleaf and Flying Saucer:

        Context context = new Context();
        context.setVariable("data", data); // Pass your data to the template

        String htmlContent = templateEngine.process("pdf_template", context); // Assuming you have a pdf_template.html

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
        }
    }
}