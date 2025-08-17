package com.wedding.backend.service;

import com.wedding.backend.dto.EventDTO;

import java.util.List;

public interface EventService {
    List<EventDTO> getEventsForGuest(String userEmail);
}
