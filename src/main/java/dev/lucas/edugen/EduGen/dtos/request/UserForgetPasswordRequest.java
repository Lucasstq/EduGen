package dev.lucas.edugen.EduGen.dtos.request;

import jakarta.validation.constraints.Email;

public record UserForgetPasswordRequest(
        @Email(message = "Email precisa ser válido")
        String email) {
}
