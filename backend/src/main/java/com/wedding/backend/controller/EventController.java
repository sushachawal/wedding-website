package com.wedding.backend.controller;

import com.wedding.backend.dto.EventDTO;
import com.wedding.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEventsForGuest(@AuthenticationPrincipal UserDetails userDetails) {
        List<EventDTO> events = eventService.getEventsForGuest(userDetails.getUsername());
        return ResponseEntity.ok(events);
    }
}
