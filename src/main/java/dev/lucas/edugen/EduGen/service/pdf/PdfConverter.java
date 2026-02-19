package dev.lucas.edugen.EduGen.service.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

@Component
public class PdfConverter {

    public byte[] htmlToPdfBytes(String html) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, getClass().getResource("/static/").toString());
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao converter HTML para PDF", e);
        }
    }
}
