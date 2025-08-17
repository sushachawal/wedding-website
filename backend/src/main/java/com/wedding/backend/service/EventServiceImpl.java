package com.wedding.backend.service;

import com.wedding.backend.dto.EventDTO;
import com.wedding.backend.model.Event;
import com.wedding.backend.model.Guest;
import com.wedding.backend.repository.EventInviteeRepository;
import com.wedding.backend.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private EventInviteeRepository eventInviteeRepository;

    @Override
    public List<EventDTO> getEventsForGuest(String userEmail) {
        Guest guest = guestRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return eventInviteeRepository.findAllByGuest(guest).stream()
                .map(eventInvitee -> toDTO(eventInvitee.getEvent()))
                .collect(Collectors.toList());
    }

    private EventDTO toDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getDate()
        );
    }
}
