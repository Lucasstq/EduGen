package dev.lucas.edugen.EduGen.dtos.request.worksheet;

import dev.lucas.edugen.EduGen.domain.enums.VersionType;
import lombok.Builder;

@Builder
public record GenerateVersionRequest(
        VersionType versionType,
        boolean includeAnswers,
        boolean includeExplanations
) {
}
