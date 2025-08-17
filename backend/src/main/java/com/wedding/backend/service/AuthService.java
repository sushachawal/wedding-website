package com.wedding.backend.service;

import com.wedding.backend.dto.LoginRequest;
import com.wedding.backend.dto.RegisterRequest;
import com.wedding.backend.dto.ChangePasswordRequest;
import com.wedding.backend.dto.PasswordResetRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    void login(LoginRequest loginRequest);
    void changePassword(String userEmail, ChangePasswordRequest changePasswordRequest);
    void resetPassword(PasswordResetRequest passwordResetRequest);
}
