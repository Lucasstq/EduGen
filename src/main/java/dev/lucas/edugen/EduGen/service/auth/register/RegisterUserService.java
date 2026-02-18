package dev.lucas.edugen.EduGen.service;

import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.dtos.request.auth.RegisterUserRequest;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import dev.lucas.edugen.EduGen.mapper.UserMapper;
import dev.lucas.edugen.EduGen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse execute(RegisterUserRequest request) {

        validateCredentials(request);

        User newUser = UserMapper.toEntity(request);
        newUser.setHashPassword(passwordEncoder.encode(request.password()));
        newUser.setActive(true);

        try {
            User savedUser = userRepository.save(newUser);
            return UserMapper.toResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Dados de usuário inválidos ou já existentes");
        }
    }

    private void validateCredentials(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("O nome de usuário já existe");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }
    }

}
