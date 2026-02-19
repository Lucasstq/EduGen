package dev.lucas.edugen.EduGen.service.storage;

import dev.lucas.edugen.EduGen.domain.enums.Audience;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${storage.base-path:./pdfs}")
    private String basePath;

    public String save(byte[] data, Long versionId, Audience audience) {
        try {
            Path dir = Paths.get(basePath, versionId.toString());
            Files.createDirectories(dir);

            String filename = audience.name().toLowerCase() + ".pdf";
            Path file = dir.resolve(filename);
            Files.write(file, data);

            return versionId + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo PDF", e);
        }
    }

    public byte[] load(String storageKey) {
        try {
            Path file = Paths.get(basePath, storageKey);
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException("PDF não encontrado: " + storageKey, e);
        }
    }

}
