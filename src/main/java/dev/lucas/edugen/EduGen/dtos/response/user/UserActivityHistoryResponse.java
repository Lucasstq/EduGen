package dev.lucas.edugen.EduGen.dtos.response.user;

import lombok.Builder;

import java.util.List;

@Builder
public record UserActivityHistoryResponse(
        List<UserActivityResponse> activities,
        int currentPage,
        int totalPages,
        long totalActivities
) {
}

