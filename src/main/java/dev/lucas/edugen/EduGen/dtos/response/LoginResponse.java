package dev.lucas.edugen.EduGen.dtos.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        long refreshExpiresIn) {
}
