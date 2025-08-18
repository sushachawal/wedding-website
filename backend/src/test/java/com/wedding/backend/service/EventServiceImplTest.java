package com.wedding.backend.service;

import com.wedding.backend.dto.EventDTO;
import com.wedding.backend.model.*;
import com.wedding.backend.repository.EventInviteeRepository;
import com.wedding.backend.repository.GuestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private GuestRepository guestRepository;
    @Mock
    private EventInviteeRepository eventInviteeRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void getEventsForGuest_shouldReturnMappedEvents() {
        Guest guest = new Guest();
        guest.setEmail("user@example.com");

        Event event = new Event();
        event.setId(1);
        event.setName("Ceremony");

        EventInvitee invitee = new EventInvitee();
        invitee.setGuest(guest);
        invitee.setEvent(event);

        when(guestRepository.findByEmail("user@example.com")).thenReturn(Optional.of(guest));
        when(eventInviteeRepository.findAllByGuest(guest)).thenReturn(List.of(invitee));

        List<EventDTO> events = eventService.getEventsForGuest("user@example.com");

        assertEquals(1, events.size());
        assertEquals("Ceremony", events.get(0).getName());
    }
}
