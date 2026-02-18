package dev.lucas.edugen.EduGen.dtos.response.worksheet;

import dev.lucas.edugen.EduGen.domain.enums.VersionStatus;
import dev.lucas.edugen.EduGen.domain.enums.VersionType;
import lombok.Builder;

@Builder
public record WorksheetVersionResponse(
        Long id,
        Long worksheetId,
        VersionType versionType,
        int seed,
        boolean includeAnswers,
        boolean includeExplanations,
        VersionStatus status
) {
}
