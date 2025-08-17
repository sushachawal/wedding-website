package com.wedding.backend.service;

import com.wedding.backend.dto.RsvpDTO;
import com.wedding.backend.dto.RsvpRequest;
import com.wedding.backend.model.Event;
import com.wedding.backend.model.Guest;
import com.wedding.backend.model.RSVP;
import com.wedding.backend.repository.EventRepository;
import com.wedding.backend.repository.GuestRepository;
import com.wedding.backend.repository.RSVPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RsvpServiceImpl implements RsvpService {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RSVPRepository rsvpRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RsvpDTO> getRsvpsForGuest(String userEmail) {
        Guest guest = guestRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return rsvpRepository.findAllByGuest(guest).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RsvpDTO createOrUpdateRsvp(String userEmail, RsvpRequest rsvpRequest) {
        Guest guest = guestRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(rsvpRequest.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        RSVP rsvp = rsvpRepository.findByGuestAndEvent(guest, event)
                .orElse(new RSVP());

        rsvp.setGuest(guest);
        rsvp.setEvent(event);
        rsvp.setStatus(rsvpRequest.getStatus());
        rsvp.setPlusOnes(rsvpRequest.getPlusOnes());
        rsvp.setComments(rsvpRequest.getComments());

        RSVP savedRsvp = rsvpRepository.save(rsvp);
        return toDTO(savedRsvp);
    }

    private RsvpDTO toDTO(RSVP rsvp) {
        RsvpDTO dto = new RsvpDTO();
        dto.setEventId(rsvp.getEvent().getId());
        dto.setEventName(rsvp.getEvent().getName());
        dto.setStatus(rsvp.getStatus());
        dto.setPlusOnes(rsvp.getPlusOnes());
        dto.setComments(rsvp.getComments());
        return dto;
    }
}
