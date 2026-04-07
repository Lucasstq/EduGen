package dev.lucas.edugen.EduGen.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.app.api-url}")
    private String baseUrl;

    @Value("${spring.app.frontend-url}")
    private String frontendUrl;

    @Async
    public void sendMail(String toEmail, String username, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Edugen");
            message.setTo(toEmail);
            message.setSubject("Só falta um passo para entrar na EduGen! 🚀");
            message.setText(buildVerificationEmailBody(username, token));

            mailSender.send(message);
            log.info("Email de verificação enviado para: {}", toEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email de verificação para: {}", toEmail, e);
            throw new RuntimeException("Erro ao enviar email de verificação", e);
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String username, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Edugen");
            message.setTo(toEmail);
            message.setSubject("Redefinição de senha solicitada 🔐");
            message.setText(buildPasswordResetEmailBody(username, token));

            mailSender.send(message);
            log.info("Email de redefinição de senha enviado para: {}", toEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email de redefinição de senha para: {}", toEmail, e);
            throw new RuntimeException("Erro ao enviar email de redefinição de senha", e);
        }
    }

    private String buildPasswordResetEmailBody(String username, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        return String.format(
                """
                        Olá, %s! 👋
                        
                        Recebemos uma solicitação para redefinir sua senha. Para garantir a segurança da sua conta, 
                        por favor, clique no link abaixo para criar uma nova senha. O link é válido por 2 horas.
                        
                        %s
                        
                        Caso não tenha sido você, ignore esta mensagem.
                        
                        Atenciosamente,
                        Suporte EduGen
                        
                        """, username, resetUrl);
    }

    private String buildVerificationEmailBody(String username, String token) {
        String confirmationUrl = baseUrl + "/api/auth/verify-email?token=" + token;

        return String.format(
                """
                        Olá, %s! 👋
                        
                        Ficamos felizes em ter você conosco. Para garantir a segurança da sua conta e
                        liberar seu acesso à plataforma EduGen o link é válido por 2 horas. 
                        Por favor, confirme seu e-mail clicando no botão abaixo:
                        
                        %s
                        
                        Caso não tenha sido você, ignore esta mensagem.
                        
                        Atenciosamente,
                        Suporte EduGen
                        
                        """, username, confirmationUrl);
    }


}
