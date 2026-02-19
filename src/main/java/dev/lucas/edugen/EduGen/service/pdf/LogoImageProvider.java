package dev.lucas.edugen.EduGen.service.pdf;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Getter
public class LogoImageProvider {

    private static final String LOGO_PATH = "classpath:/static/logo_escola.jpeg";

    private final ResourceLoader resourceLoader;

    private String dataUri;

    @PostConstruct
    private void loadLogo() {
        Resource resource = resourceLoader.getResource(LOGO_PATH);
        try (InputStream input = resource.getInputStream()) {
            byte[] bytes = input.readAllBytes();
            dataUri = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load logo for PDF generation", e);
        }
    }
}

