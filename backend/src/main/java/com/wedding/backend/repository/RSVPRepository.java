package com.wedding.backend.repository;

import com.wedding.backend.model.Event;
import com.wedding.backend.model.Guest;
import com.wedding.backend.model.RSVP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RSVPRepository extends JpaRepository<RSVP, Long> {
    List<RSVP> findAllByGuest(Guest guest);
    Optional<RSVP> findByGuestAndEvent(Guest guest, Event event);
}
