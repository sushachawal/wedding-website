package com.wedding.backend.controller;

import com.wedding.backend.dto.LoginRequest;
import com.wedding.backend.dto.RegisterRequest;
import com.wedding.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
        return ResponseEntity.ok("Login successful");
    }

    // @PostMapping("/change-password")
    // public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordRequest changePasswordRequest) {
    //     authService.changePassword(userDetails.getUsername(), changePasswordRequest);
    //     return ResponseEntity.ok("Password changed successfully");
    // }

    // @PostMapping("/reset-password")
    // public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
    //     authService.resetPassword(passwordResetRequest);
    //     return ResponseEntity.ok("Password reset successful");
    // }
}
