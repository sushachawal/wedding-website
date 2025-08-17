package com.wedding.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
public class RegisterRequest {
    @NotBlank(message="Invite code is required")
    private String inviteCode;
    @Email(message="Email is required")
    private String email;
    @NotBlank(message="Password is required")
    private String password;
}
