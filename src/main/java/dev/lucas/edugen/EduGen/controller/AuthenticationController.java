package dev.lucas.edugen.EduGen.controller;

import dev.lucas.edugen.EduGen.dtos.request.auth.LoginUserRequest;
import dev.lucas.edugen.EduGen.dtos.request.auth.RegisterUserRequest;
import dev.lucas.edugen.EduGen.dtos.response.LoginResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.EmailTokenInvalidException;
import dev.lucas.edugen.EduGen.service.auth.register.RegisterUserService;
import dev.lucas.edugen.EduGen.service.auth.login.LoginUserService;
import dev.lucas.edugen.EduGen.service.mail.EmailVerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final LoginUserService loginUserService;
    private final RegisterUserService registerUserService;
    private final EmailVerificationService emailVerificationService;

    @Value("${spring.app.frontend-url}")
    private String frontendUrl;

    public AuthenticationController(LoginUserService loginUserService, RegisterUserService registerUserService, EmailVerificationService emailVerificationService) {
        this.loginUserService = loginUserService;
        this.registerUserService = registerUserService;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        return ResponseEntity.ok().body(registerUserService.execute(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        try {
            emailVerificationService.verifyEmail(token);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/login?verified=true"))
                    .build();
        } catch (EmailTokenInvalidException ex) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/login?verified=false&error=" + ex.getMessage()))
                    .build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest request) {
        return ResponseEntity.ok().body(loginUserService.execute(request));
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok().body(loginUserService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        loginUserService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
