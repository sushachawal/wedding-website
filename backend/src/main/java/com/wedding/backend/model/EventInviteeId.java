package com.wedding.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class EventInviteeId implements Serializable {

    private Integer eventId;
    private Integer guestId;

}
