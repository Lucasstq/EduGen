package dev.lucas.edugen.EduGen.controller;

import dev.lucas.edugen.EduGen.dtos.request.LoginUserRequest;
import dev.lucas.edugen.EduGen.dtos.request.RegisterUserRequest;
import dev.lucas.edugen.EduGen.dtos.response.LoginResponse;
import dev.lucas.edugen.EduGen.dtos.response.user.UserResponse;
import dev.lucas.edugen.EduGen.service.RegisterUserService;
import dev.lucas.edugen.EduGen.service.auth.login.LoginUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final LoginUserService loginUserService;
    private final RegisterUserService registerUserService;

    public AuthenticationController(LoginUserService loginUserService, RegisterUserService registerUserService) {
        this.loginUserService = loginUserService;
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        return ResponseEntity.ok().body(registerUserService.execute(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest request) {
        return ResponseEntity.ok().body(loginUserService.execute(request));
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok().body(loginUserService.refreshToken(refreshToken));
    }
}
