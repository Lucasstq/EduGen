package dev.lucas.edugen.EduGen.service.auth.refreshToken;

import dev.lucas.edugen.EduGen.domain.RefreshToken;
import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Getter
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.expiration:604800}")
    private Long refreshTokenExpiration;

    public RefreshToken generateRefreshToken(User user) {
        String token = java.util.UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(refreshTokenExpiration))
                .createdAt(LocalDateTime.now())
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken;

    }

    public void saveRevokedToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isExpired() || refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token is expired or revoked");
        }

        return refreshToken;
    }

    public void cleanupExpiredTokens(){
        refreshTokenRepository.deleteExpiredTokens();
    }

}
