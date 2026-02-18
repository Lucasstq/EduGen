package dev.lucas.edugen.EduGen.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginUserRequest (
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,
        @NotBlank(message = "A senha é obrigatória")
        String password
){
}
