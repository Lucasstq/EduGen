package dev.lucas.edugen.EduGen.service.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import dev.lucas.edugen.EduGen.eduGenException.infrastructureException.PdfGenerationException;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PdfConverter {

    public byte[] htmlToPdfBytes(String html) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, getClass().getResource("/static/").toString());
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new PdfGenerationException();
        }
    }
}
