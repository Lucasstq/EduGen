package dev.lucas.edugen.EduGen.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterUserRequest(
        @NotBlank(message = "Nome de usuário é obrigatório")
        String username,

        @Email(message = "Email deve ser válido")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 100, message = "A senha deve conter entre 8 e 100 caracteres")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "A senha deve conter pelo menos uma letra minúscula, uma letra maiúscula e um dígito")
        String password,

        @NotBlank(message = "Confirmação de senha é obrigatória")
        String confirmPassword
) {
}
