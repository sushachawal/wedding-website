package com.wedding.backend.controller;

import com.wedding.backend.dto.RsvpDTO;
import com.wedding.backend.dto.RsvpRequest;
import com.wedding.backend.service.RsvpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rsvps")
public class RsvpController {

    @Autowired
    private RsvpService rsvpService;

    @GetMapping
    public ResponseEntity<List<RsvpDTO>> getRsvpsForGuest(@AuthenticationPrincipal UserDetails userDetails) {
        List<RsvpDTO> rsvps = rsvpService.getRsvpsForGuest(userDetails.getUsername());
        return ResponseEntity.ok(rsvps);
    }

    @PostMapping
    public ResponseEntity<RsvpDTO> createOrUpdateRsvp(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RsvpRequest rsvpRequest) {
        RsvpDTO rsvp = rsvpService.createOrUpdateRsvp(userDetails.getUsername(), rsvpRequest);
        return ResponseEntity.ok(rsvp);
    }
}
