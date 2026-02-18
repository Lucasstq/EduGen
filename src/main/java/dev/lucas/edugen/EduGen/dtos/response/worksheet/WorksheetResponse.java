package dev.lucas.edugen.EduGen.dtos.response.worksheet;

import dev.lucas.edugen.EduGen.domain.enums.Difficulty;
import dev.lucas.edugen.EduGen.domain.enums.Grade;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorksheetResponse(
        Long id,
        String teacherName,
        Subject subject,
        Grade grade,
        String topic,
        Difficulty difficulty,
        int questionCount,
        LocalDateTime createdAt
) {
}
