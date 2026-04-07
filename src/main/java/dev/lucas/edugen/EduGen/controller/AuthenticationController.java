package dev.lucas.edugen.EduGen.controller;

import dev.lucas.edugen.EduGen.dtos.request.UserChangePasswordRequest;
import dev.lucas.edugen.EduGen.dtos.request.UserForgetPasswordRequest;
import dev.lucas.edugen.EduGen.dtos.request.auth.LoginUserRequest;
import dev.lucas.edugen.EduGen.dtos.request.auth.RegisterUserRequest;
import dev.lucas.edugen.EduGen.dtos.response.LoginResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.EmailTokenInvalidException;
import dev.lucas.edugen.EduGen.service.auth.register.RegisterUserService;
import dev.lucas.edugen.EduGen.service.auth.login.LoginUserService;
import dev.lucas.edugen.EduGen.service.mail.AuthMailService;
import dev.lucas.edugen.EduGen.service.user.ResetPasswordUserService;
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
    private final AuthMailService authMailService;
    private final ResetPasswordUserService resetPasswordUserService;

    @Value("${spring.app.frontend-url}")
    private String frontendUrl;

    public AuthenticationController(LoginUserService loginUserService, RegisterUserService registerUserService,
                                    AuthMailService authMailService, ResetPasswordUserService resetPasswordUserService) {
        this.loginUserService = loginUserService;
        this.registerUserService = registerUserService;
        this.authMailService = authMailService;
        this.resetPasswordUserService = resetPasswordUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        return ResponseEntity.ok().body(registerUserService.execute(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        try {
            authMailService.verifyEmail(token);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/login?verified=true"))
                    .build();
        } catch (EmailTokenInvalidException ex) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/login?verified=false&error=" + ex.getMessage()))
                    .build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody UserForgetPasswordRequest request) {
        authMailService.sendPasswordResetEmail(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestBody @Valid UserChangePasswordRequest request) {
        resetPasswordUserService.resetPassword(token, request);
        return ResponseEntity.noContent().build();
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
