package com.wedding.backend.service;

import com.wedding.backend.dto.*;
import com.wedding.backend.model.*;
import com.wedding.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private GuestRepository guestRepository;
    @Mock
    private InviteCodeRepository inviteCodeRepository;
    @Mock
    private ResetCodeRepository resetCodeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_shouldPersistGuestAndConsumeInvite() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setInviteCode("invite");

        Guest guest = new Guest();
        guest.setEmail("test@example.com");

        InviteCode inviteCode = new InviteCode();
        inviteCode.setCodeHash("invite");
        inviteCode.setStatus("active");
        inviteCode.setExpiresAt(Instant.now().plusSeconds(3600));

        when(guestRepository.findByEmail("test@example.com")).thenReturn(Optional.of(guest));
        when(inviteCodeRepository.findByGuestAndStatus(guest, "active")).thenReturn(Optional.of(inviteCode));
        when(passwordEncoder.encode("password")).thenReturn("hashed");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        authService.register(request);

        assertEquals("hashed", guest.getPasswordHash());
        assertEquals("consumed", inviteCode.getStatus());
        verify(guestRepository).save(guest);
        verify(inviteCodeRepository).save(inviteCode);
        verify(authenticationManager).authenticate(any());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void login_shouldSetAuthenticationInContext() {
        LoginRequest request = new LoginRequest("user@example.com", "pw");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        authService.login(request);

        verify(authenticationManager).authenticate(any());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void changePassword_shouldUpdateHash() {
        Guest guest = new Guest();
        guest.setEmail("user@example.com");
        guest.setPasswordHash("oldHash");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("old");
        request.setNewPassword("new");

        when(guestRepository.findByEmail("user@example.com")).thenReturn(Optional.of(guest));
        when(passwordEncoder.matches("old", "oldHash")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("newHash");

        authService.changePassword("user@example.com", request);

        assertEquals("newHash", guest.getPasswordHash());
        verify(guestRepository).save(guest);
    }

    @Test
    void resetPassword_shouldPersistAndAuthenticate() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("user@example.com");
        request.setResetCode("code");
        request.setNewPassword("new");

        Guest guest = new Guest();
        guest.setEmail("user@example.com");

        ResetCode resetCode = new ResetCode();
        resetCode.setCodeHash("hash");
        resetCode.setStatus("active");
        resetCode.setExpiresAt(Instant.now().plusSeconds(3600));

        when(guestRepository.findByEmail("user@example.com")).thenReturn(Optional.of(guest));
        when(resetCodeRepository.findByGuestAndStatus(guest, "active")).thenReturn(Optional.of(resetCode));
        when(passwordEncoder.matches("code", "hash")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encoded");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        authService.resetPassword(request);

        assertEquals("encoded", guest.getPasswordHash());
        assertEquals("consumed", resetCode.getStatus());
        verify(guestRepository).save(guest);
        verify(resetCodeRepository).save(resetCode);
        verify(authenticationManager).authenticate(any());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }
}

