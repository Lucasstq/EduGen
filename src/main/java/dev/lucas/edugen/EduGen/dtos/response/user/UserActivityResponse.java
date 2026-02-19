package dev.lucas.edugen.EduGen.dtos.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserActivityResponse(
        @JsonProperty("version_id")
        Long versionId,
        @JsonProperty("worksheet_topic")
        String worksheetTopic,
        String subject,
        String grade,
        String difficulty,
        @JsonProperty("question_count")
        int questionCount,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
}

