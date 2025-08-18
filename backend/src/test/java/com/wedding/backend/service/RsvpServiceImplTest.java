package com.wedding.backend.service;

import com.wedding.backend.dto.RsvpDTO;
import com.wedding.backend.dto.RsvpRequest;
import com.wedding.backend.model.*;
import com.wedding.backend.repository.EventRepository;
import com.wedding.backend.repository.GuestRepository;
import com.wedding.backend.repository.RSVPRepository;
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
class RsvpServiceImplTest {

    @Mock
    private GuestRepository guestRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private RSVPRepository rsvpRepository;

    @InjectMocks
    private RsvpServiceImpl rsvpService;

    @Test
    void getRsvpsForGuest_shouldReturnDtos() {
        Guest guest = new Guest();
        guest.setEmail("user@example.com");

        Event event = new Event();
        event.setId(1);
        event.setName("Reception");

        RSVP rsvp = new RSVP();
        rsvp.setGuest(guest);
        rsvp.setEvent(event);
        rsvp.setStatus("YES");
        rsvp.setPlusOnes(2);
        rsvp.setComments("Looking forward");

        when(guestRepository.findByEmail("user@example.com")).thenReturn(Optional.of(guest));
        when(rsvpRepository.findAllByGuest(guest)).thenReturn(List.of(rsvp));

        List<RsvpDTO> dtos = rsvpService.getRsvpsForGuest("user@example.com");

        assertEquals(1, dtos.size());
        assertEquals("Reception", dtos.get(0).getEventName());
        assertEquals("YES", dtos.get(0).getStatus());
    }

    @Test
    void createOrUpdateRsvp_shouldSaveAndReturnDto() {
        Guest guest = new Guest();
        guest.setEmail("user@example.com");

        Event event = new Event();
        event.setId(1);
        event.setName("Reception");

        when(guestRepository.findByEmail("user@example.com")).thenReturn(Optional.of(guest));
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(rsvpRepository.findByGuestAndEvent(guest, event)).thenReturn(Optional.empty());
        when(rsvpRepository.save(any(RSVP.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RsvpRequest request = new RsvpRequest();
        request.setEventId(1);
        request.setStatus("YES");
        request.setPlusOnes(1);
        request.setComments("Comment");

        RsvpDTO dto = rsvpService.createOrUpdateRsvp("user@example.com", request);

        verify(rsvpRepository).save(any(RSVP.class));
        assertEquals("YES", dto.getStatus());
        assertEquals("Reception", dto.getEventName());
        assertEquals(1, dto.getPlusOnes());
    }
}
