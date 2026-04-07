package dev.lucas.edugen.EduGen.service.mail;

import dev.lucas.edugen.EduGen.domain.PasswordResetToken;
import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.dtos.request.UserForgetPasswordRequest;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.EmailException;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.EmailTokenInvalidException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.UserNotFoundException;
import dev.lucas.edugen.EduGen.repository.PasswordResetTokenRepository;
import dev.lucas.edugen.EduGen.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    // Tempo de validade do token de verificação em horas
    private static final int TOKEN_EXPIRATION_HOURS = 2;

    @Transactional
    public void sendVerificationEmail(User user) {
        // Gera um token único usando UUID
        String token = UUID.randomUUID().toString();

        // Calcula quando o token irá expirar (2h a partir de agora)
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS);

        // Atualiza o usuário com o novo token e data de expiração
        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenExpiresAt(expiresAt);
        userRepository.save(user);

        // Delega o envio físico do email para o EmailService
        emailService.sendMail(user.getEmail(), user.getUsername(), token);
        log.info("Email de verificação agendado para o usuário: {}", user.getUsername());
    }

    @Transactional
    public void sendPasswordResetEmail(UserForgetPasswordRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(u -> {
                    String token = UUID.randomUUID().toString();
                    LocalDateTime expiresAt = LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS);

                    PasswordResetToken passwordResetToken = new PasswordResetToken();
                    passwordResetToken.setToken(token);
                    passwordResetToken.setExpiryDate(expiresAt);
                    passwordResetToken.setUser(u);

                    passwordResetTokenRepository.save(passwordResetToken);

                    emailService.sendPasswordResetEmail(u.getEmail(), u.getUsername(), token);
                    log.info("Email de redefinição de senha agendado para o usuário: {}", u.getUsername());
                });

        log.info("Processamento de esqueci minha senha finalizado para o e-mail informado.");

    }

    // Verifica o token de email, ativa a conta do usuário e limpa os campos relacionados
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new EmailTokenInvalidException("Token de verificação inválido"));

        // Verifica se o token expirou
        if (user.getEmailVerificationTokenExpiresAt() != null &&
                user.getEmailVerificationTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new EmailTokenInvalidException("Token de verificação expirado");
        }

        // Ativa a conta do usuário e limpa os campos de token
        user.setActive(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiresAt(null);
        userRepository.save(user);
        log.info("Usuário {} ativado com sucesso", user.getUsername());
    }
}
