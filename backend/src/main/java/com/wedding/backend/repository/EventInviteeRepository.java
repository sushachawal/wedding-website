package com.wedding.backend.repository;

import com.wedding.backend.model.EventInvitee;
import com.wedding.backend.model.Guest;
import com.wedding.backend.model.EventInviteeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventInviteeRepository extends JpaRepository<EventInvitee, EventInviteeId> {
    List<EventInvitee> findAllByGuest(Guest guest);
}
