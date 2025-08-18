package com.wedding.backend.service;

import com.wedding.backend.model.Guest;
import com.wedding.backend.repository.GuestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnAdminRole() {
        Guest guest = new Guest();
        guest.setEmail("admin@example.com");
        guest.setPasswordHash("hash");
        guest.setAdmin(true);

        when(guestRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(guest));

        UserDetails details = userDetailsService.loadUserByUsername("admin@example.com");

        assertEquals("admin@example.com", details.getUsername());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_shouldReturnUserRole() {
        Guest guest = new Guest();
        guest.setEmail("user@example.com");
        guest.setPasswordHash("hash");
        guest.setAdmin(false);

        when(guestRepository.findByEmail("user@example.com")).thenReturn(Optional.of(guest));

        UserDetails details = userDetailsService.loadUserByUsername("user@example.com");

        assertEquals("user@example.com", details.getUsername());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}
