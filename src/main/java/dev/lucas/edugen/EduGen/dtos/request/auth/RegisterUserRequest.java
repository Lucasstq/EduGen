package dev.lucas.edugen.EduGen.dtos.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterUserRequest(
        @NotBlank(message = "Nome de usuário é obrigatório")
        @JsonProperty("username")
        String username,

        @Email(message = "Email deve ser válido")
        @NotBlank(message = "Email é obrigatório")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 100, message = "A senha deve conter entre 8 e 100 caracteres")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "A senha deve conter pelo menos uma letra minúscula, uma letra maiúscula e um dígito")
        @JsonProperty("password")
        String password,

        @NotBlank(message = "Confirmação de senha é obrigatória")
        @JsonProperty("confirmPassword")
        String confirmPassword
) {
}
