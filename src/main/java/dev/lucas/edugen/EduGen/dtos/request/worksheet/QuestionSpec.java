package dev.lucas.edugen.EduGen.dtos.request.worksheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record QuestionSpec(
        Integer orderNumber,
        String type,
        String statement,
        String correctAnswer,
        String explanation,
        List<OptionSpec> options
) {
}
