package com.wedding.backend.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String resetCode;
    private String email;
    private String newPassword;
}
