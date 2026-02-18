package dev.lucas.edugen.EduGen.service.worksheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.edugen.EduGen.domain.Worksheet;
import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import dev.lucas.edugen.EduGen.domain.enums.VersionStatus;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.CreateWorksheetRequest;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.GenerateVersionRequest;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.WorksheetSpec;
import dev.lucas.edugen.EduGen.dtos.response.worksheet.WorksheetResponse;
import dev.lucas.edugen.EduGen.dtos.response.worksheet.WorksheetVersionResponse;
import dev.lucas.edugen.EduGen.mapper.WorksheetSpecEntityMapper;
import dev.lucas.edugen.EduGen.repository.UserRepository;
import dev.lucas.edugen.EduGen.repository.WorksheetRepository;
import dev.lucas.edugen.EduGen.repository.WorksheetVersionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class WorksheetService {

    private final WorksheetRepository worksheetRepository;
    private final WorksheetVersionRepository worksheetVersionRepository;
    private final UserRepository userRepository;

    private final AiWorksheetService aiWorksheetService;
    private final ObjectMapper objectMapper;

    public WorksheetVersionResponse generateWorksheetVersion(Long worksheetId, GenerateVersionRequest req,
                                                             UUID userId) {
        Worksheet worksheet = worksheetRepository.findByIdAndOwnerId(worksheetId, userId)
                .orElseThrow(() -> new RuntimeException("Worksheet não encontrada."));

        WorksheetVersion v = WorksheetVersion.builder()
                .worksheet(worksheet)
                .versionType(req.versionType())
                .includeAnswers(req.includeAnswers())
                .includeExplanations(req.includeExplanations())
                .seed(ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE))
                .status(VersionStatus.DRAFT)
                .build();

        v = worksheetVersionRepository.save(v);

        try {
            String rawJson = aiWorksheetService.generateSpec(worksheet, v);

            WorksheetSpec spec = objectMapper.readValue(rawJson, WorksheetSpec.class);

            v.setSpecJson(objectMapper.writeValueAsString(spec));

            WorksheetSpecEntityMapper.fillQuestions(v, spec);
            v.setStatus(VersionStatus.GENERATED);

            v = worksheetVersionRepository.save(v);

            return WorksheetVersionResponse.builder()
                    .id(v.getId())
                    .worksheetId(worksheet.getId())
                    .versionType(v.getVersionType())
                    .seed(v.getSeed())
                    .includeAnswers(v.isIncludeAnswers())
                    .includeExplanations(v.isIncludeExplanations())
                    .status(v.getStatus())
                    .build();
        } catch (Exception e) {
            v.setStatus(VersionStatus.FAILED);
            worksheetVersionRepository.save(v);
            throw new IllegalStateException("Falha ao gerar atividade", e);
        }
    }

    public WorksheetResponse createWorksheet(CreateWorksheetRequest req, UUID userId) {
        Worksheet w = Worksheet.builder()
                .owner(userRepository.findById(userId).orElseThrow())
                .teacherName(null)
                .subject(req.subject())
                .grade(req.grade())
                .topic(req.topic().trim())
                .difficulty(req.difficulty())
                .questionCount(req.questionCount())
                .build();

        w = worksheetRepository.save(w);

        return WorksheetResponse.builder()
                .id(w.getId())
                .subject(w.getSubject())
                .grade(w.getGrade())
                .topic(w.getTopic())
                .difficulty(w.getDifficulty())
                .questionCount(w.getQuestionCount())
                .build();
    }

    public String getSpecJson(Long versionId, UUID userId) {
        WorksheetVersion v = worksheetVersionRepository
                .findByIdAndWorksheetOwnerId(versionId, userId)
                .orElseThrow(() -> new RuntimeException("Versão não encontrada"));

        String spec = v.getSpecJson();
        if (spec == null || spec.isBlank()) {
            throw new RuntimeException("Spec ainda não foi gerado");
        }

        return spec;
    }

}
