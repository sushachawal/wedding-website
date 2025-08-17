package com.wedding.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RsvpDTO {
    private Integer eventId;
    private String eventName;
    private String status;
    private Integer plusOnes;
    private String comments;
}
