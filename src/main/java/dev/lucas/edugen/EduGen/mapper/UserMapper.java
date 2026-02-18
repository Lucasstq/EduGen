package dev.lucas.edugen.EduGen.mapper;

import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.dtos.request.auth.RegisterUserRequest;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toEntity(RegisterUserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .hashPassword(request.password())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }


}
