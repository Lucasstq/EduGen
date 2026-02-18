package dev.lucas.edugen.EduGen.dtos.response.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
