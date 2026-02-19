package dev.lucas.edugen.EduGen.service;

import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.domain.Worksheet;
import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import dev.lucas.edugen.EduGen.dtos.request.UpdateProfileRequest;
import dev.lucas.edugen.EduGen.dtos.response.user.UserActivityHistoryResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserActivityResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserDashBoardResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import dev.lucas.edugen.EduGen.mapper.UserMapper;
import dev.lucas.edugen.EduGen.repository.UserRepository;
import dev.lucas.edugen.EduGen.repository.WorksheetRepository;
import dev.lucas.edugen.EduGen.repository.WorksheetVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WorksheetRepository worksheetRepository;
    private final WorksheetVersionRepository worksheetVersionRepository;

    public UserDashBoardResponse getDashboard(UUID userId) {
        User user = requireUser(userId);

        long totalWorksheets = worksheetRepository.countWorksheetByOwner(user);
        long totalQuestions = worksheetRepository.countQuestionsByOwner(userId);

        return UserDashBoardResponse.builder()
                .username(user.getUsername())
                .totalWorksheets(totalWorksheets)
                .totalExercises(totalQuestions)
                .build();
    }

    public List<UserActivityResponse> getLatestActivities(UUID userId) {
        requireUser(userId);
        return worksheetVersionRepository
                .findTop3ByWorksheetOwnerIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toActivityResponse)
                .toList();
    }

    public UserActivityHistoryResponse getActivityHistory(UUID userId, Subject subject, int page, int size) {
        requireUser(userId);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<WorksheetVersion> history = subject == null
                ? worksheetVersionRepository.findByWorksheetOwnerId(userId, pageable)
                : worksheetVersionRepository.findByWorksheetOwnerIdAndWorksheetSubject(userId, subject, pageable);

        List<UserActivityResponse> activities = history
                .stream()
                .map(this::toActivityResponse)
                .toList();

        return UserActivityHistoryResponse.builder()
                .activities(activities)
                .currentPage(history.getNumber())
                .totalPages(history.getTotalPages())
                .totalActivities(history.getTotalElements())
                .build();
    }

    public UserResponse getProfile(UUID userId) {
        User user = requireUser(userId);
        return UserMapper.toResponse(user);
    }

    public UserResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = requireUser(userId);
        if (request.username() != null) user.setUsername(request.username());
        // TODO: email só com confirmação de senha
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    private UserActivityResponse toActivityResponse(WorksheetVersion version) {
        Worksheet worksheet = version.getWorksheet();
        return UserActivityResponse.builder()
                .versionId(version.getId())
                .worksheetTopic(worksheet.getTopic())
                .subject(worksheet.getSubject().name())
                .grade(worksheet.getGrade().name())
                .difficulty(worksheet.getDifficulty().name())
                .questionCount(worksheet.getQuestionCount())
                .createdAt(version.getCreatedAt())
                .build();
    }
}
