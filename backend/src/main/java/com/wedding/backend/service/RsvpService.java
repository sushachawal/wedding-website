package com.wedding.backend.service;

import com.wedding.backend.dto.RsvpDTO;
import com.wedding.backend.dto.RsvpRequest;

import java.util.List;

public interface RsvpService {
    List<RsvpDTO> getRsvpsForGuest(String userEmail);
    RsvpDTO createOrUpdateRsvp(String userEmail, RsvpRequest rsvpRequest);
}
