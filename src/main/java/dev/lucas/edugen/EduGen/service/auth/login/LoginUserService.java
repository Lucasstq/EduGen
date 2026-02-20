package dev.lucas.edugen.EduGen.service.auth.login;

import dev.lucas.edugen.EduGen.domain.RefreshToken;
import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.dtos.request.auth.LoginUserRequest;
import dev.lucas.edugen.EduGen.dtos.response.LoginResponse;
import dev.lucas.edugen.EduGen.repository.UserRepository;
import dev.lucas.edugen.EduGen.service.auth.jwt.JwtTokenService;
import dev.lucas.edugen.EduGen.service.auth.refreshToken.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService {

    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public LoginResponse execute(LoginUserRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Usuário inativo. Por favor, ative sua conta antes de fazer login.");
        }

        if (!passwordEncoder.matches(request.password(), user.getHashPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }

        String accessToken = jwtTokenService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);

        return LoginResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(jwtTokenService.getAccessTokenExpiration())
                .refreshExpiresIn(refreshTokenService.getRefreshTokenExpiration())
                .build();
    }

    /*
     * Valida o refresh token, revoga o antigo e gera um novo par de tokens
     */
    public LoginResponse refreshToken(String refreshTokenStr) {
        RefreshToken oldToken = refreshTokenService.validateRefreshToken(refreshTokenStr);
        User user = oldToken.getUser();

        refreshTokenService.saveRevokedToken(oldToken);

        RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken(user);
        String newAccessToken = jwtTokenService.generateAccessToken(user);

        return LoginResponse.builder()
                .tokenType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(jwtTokenService.getAccessTokenExpiration())
                .refreshExpiresIn(refreshTokenService.getRefreshTokenExpiration())
                .build();
    }

    // Logout
    public void logout(String refreshToken){
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);

        refreshTokenService.revokeRefreshToken(token.getToken());
    }


}
