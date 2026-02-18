package dev.lucas.edugen.EduGen.dtos.request.worksheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record OptionSpec(String label, String text) {
}
