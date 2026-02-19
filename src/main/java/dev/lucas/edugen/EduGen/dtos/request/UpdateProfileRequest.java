package dev.lucas.edugen.EduGen.dtos.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateProfileRequest(
        @Size(min = 3) String username
) {

}
