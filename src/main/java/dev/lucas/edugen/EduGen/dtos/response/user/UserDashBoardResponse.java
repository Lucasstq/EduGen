package dev.lucas.edugen.EduGen.dtos.response.user;

import lombok.Builder;

@Builder
public record UserDashBoardResponse(
        String username,
        long totalWorksheets,
        long totalExercises
){
}
