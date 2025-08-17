package com.wedding.backend.service;

import com.wedding.backend.dto.ChangePasswordRequest;
import com.wedding.backend.dto.LoginRequest;
import com.wedding.backend.dto.PasswordResetRequest;
import com.wedding.backend.dto.RegisterRequest;
import com.wedding.backend.model.Guest;
import com.wedding.backend.model.InviteCode;
import com.wedding.backend.model.ResetCode;
import com.wedding.backend.repository.GuestRepository;
import com.wedding.backend.repository.InviteCodeRepository;
import com.wedding.backend.repository.ResetCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private InviteCodeRepository inviteCodeRepository;
    @Autowired
    private ResetCodeRepository resetCodeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        Guest guest = guestRepository.findByEmail(registerRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Guest not found with this email."));

        if (guest.getPasswordHash() != null && !guest.getPasswordHash().isEmpty()) {
            throw new RuntimeException("Guest is already registered.");
        }

        InviteCode inviteCode = inviteCodeRepository.findByGuestAndStatus(guest, "active")
                .orElseThrow(() -> new RuntimeException("No active invite code found for this guest."));

        if (!registerRequest.getInviteCode().equals(inviteCode.getCodeHash())) {
            throw new RuntimeException("Invalid invite code.");
        }

        if (inviteCode.getExpiresAt() != null && inviteCode.getExpiresAt().isBefore(Instant.now())) {
            inviteCode.setStatus("expired");
            inviteCodeRepository.save(inviteCode);
            throw new RuntimeException("Invite code has expired.");
        }

        guest.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        guestRepository.save(guest);

        inviteCode.setStatus("consumed");
        inviteCodeRepository.save(inviteCode);

        // Log the user in after registration
        login(new LoginRequest(registerRequest.getEmail(), registerRequest.getPassword()));
    }

    @Override
    public void login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    @Transactional
    public void changePassword(String userEmail, ChangePasswordRequest changePasswordRequest) {
        Guest guest = guestRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), guest.getPasswordHash())) {
            throw new RuntimeException("Incorrect current password.");
        }

        guest.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        guestRepository.save(guest);
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        Guest guest = guestRepository.findByEmail(passwordResetRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Guest not found with this email."));

        ResetCode resetCode = resetCodeRepository.findByGuestAndStatus(guest, "active")
                .orElseThrow(() -> new RuntimeException("No active reset code found for this guest."));

        if (!passwordEncoder.matches(passwordResetRequest.getResetCode(), resetCode.getCodeHash())) {
            throw new RuntimeException("Invalid reset code.");
        }

        if (resetCode.getExpiresAt() != null && resetCode.getExpiresAt().isBefore(Instant.now())) {
            resetCode.setStatus("expired");
            resetCodeRepository.save(resetCode);
            throw new RuntimeException("Reset code has expired.");
        }

        guest.setPasswordHash(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        guestRepository.save(guest);

        resetCode.setStatus("consumed");
        resetCodeRepository.save(resetCode);

        // Log the user in after password reset
        login(new LoginRequest(passwordResetRequest.getEmail(), passwordResetRequest.getNewPassword()));
    }
}
