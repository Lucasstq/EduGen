package dev.lucas.edugen.EduGen.dtos.request.worksheet;

import dev.lucas.edugen.EduGen.domain.enums.Difficulty;
import dev.lucas.edugen.EduGen.domain.enums.Grade;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import lombok.Builder;

@Builder
public record CreateWorksheetRequest(
        Subject subject,
        Grade grade,
        String topic,
        Difficulty difficulty,
        int questionCount
) {
}
