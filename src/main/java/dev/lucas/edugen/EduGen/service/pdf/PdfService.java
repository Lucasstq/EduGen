package dev.lucas.edugen.EduGen.service.pdf;

import dev.lucas.edugen.EduGen.domain.WorksheetFile;
import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import dev.lucas.edugen.EduGen.domain.enums.Audience;
import dev.lucas.edugen.EduGen.domain.enums.VersionStatus;
import dev.lucas.edugen.EduGen.repository.WorksheetFileRepository;
import dev.lucas.edugen.EduGen.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final ThymeleafPdfRenderer renderer;
    private final PdfConverter converter;
    private final StorageService storageService;
    private final WorksheetFileRepository fileRepository;

    public void renderAndStore(WorksheetVersion version){
        renderFor(version, Audience.STUDENTS, "worksheet_student", false, false);
        renderFor(version, Audience.TEACHERS, "worksheet_teacher", version.isIncludeAnswers(), version.isIncludeExplanations());

        version.setStatus(VersionStatus.RENDERED);
    }

    private void renderFor(WorksheetVersion version, Audience audience, String template,
                           boolean showAnswers, boolean showExplanations) {
        String html = renderer.toHtml(version, template, showAnswers, showExplanations);
        byte[] pdf = converter.htmlToPdfBytes(html);
        String key = storageService.save(pdf, version.getId(), audience);

        WorksheetFile file = WorksheetFile.builder()
                .version(version)
                .audience(audience)
                .storageKey(key)
                .sizeBytes(pdf.length)
                .sha256(computeSha256(pdf))
                .build();

        fileRepository.save(file);
    }

    private String computeSha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            return "";
        }
    }


}
