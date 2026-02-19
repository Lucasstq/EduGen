package dev.lucas.edugen.EduGen.controller;

import dev.lucas.edugen.EduGen.dtos.request.UpdateProfileRequest;
import dev.lucas.edugen.EduGen.dtos.response.user.UserActivityHistoryResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserActivityResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserDashBoardResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import dev.lucas.edugen.EduGen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me/dashboard")
    public ResponseEntity<UserDashBoardResponse> dashboard(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.ok(userService.getDashboard(userId));
    }

    @GetMapping("/me/dashboard/activities/latest")
    public ResponseEntity<List<UserActivityResponse>> latestActivities(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.ok(userService.getLatestActivities(userId));
    }

    @GetMapping("/me/dashboard/activities")
    public ResponseEntity<UserActivityHistoryResponse> activityHistory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) Subject subject
    ) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.ok(userService.getActivityHistory(userId, subject, page, size));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody @Valid UpdateProfileRequest request,
                                                      @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }
}
