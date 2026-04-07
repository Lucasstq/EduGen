package dev.lucas.edugen.EduGen.service.user;

import dev.lucas.edugen.EduGen.domain.PasswordResetToken;
import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.dtos.request.UserChangePasswordRequest;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.EmailTokenInvalidException;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.PasswordMismatchException;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.PasswordMustDifferentException;
import dev.lucas.edugen.EduGen.repository.PasswordResetTokenRepository;
import dev.lucas.edugen.EduGen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordUserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(String token, UserChangePasswordRequest request) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new EmailTokenInvalidException("Link de redefinição de senha inválido ou expirado"));

        if (passwordResetToken.isExpired()) {
            throw new EmailTokenInvalidException("Link de redefinição de senha expirado");
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new PasswordMismatchException();
        }

        User user = passwordResetToken.getUser();

        if (passwordEncoder.matches(request.newPassword(), user.getHashPassword())) {
            throw new PasswordMustDifferentException();
        }

        user.setHashPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

}
