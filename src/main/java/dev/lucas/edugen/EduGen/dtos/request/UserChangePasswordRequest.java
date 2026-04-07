package dev.lucas.edugen.EduGen.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 100, message = "A senha deve conter entre 8 e 100 caracteres")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "A senha deve conter pelo menos uma letra minúscula, uma letra maiúscula e um dígito")
        @JsonProperty("newPassword")
        String newPassword,
        @NotBlank(message = "Confirmação de senha é obrigatória")
        @JsonProperty("confirmNewPassword")
        String confirmNewPassword
) {
}
