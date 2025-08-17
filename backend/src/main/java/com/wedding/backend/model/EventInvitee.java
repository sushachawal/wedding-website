package com.wedding.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "event_invitees")
public class EventInvitee {

    @EmbeddedId
    private EventInviteeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guestId")
    @JoinColumn(name = "guest_id")
    private Guest guest;

}
