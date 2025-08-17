package com.wedding.backend.dto;

import lombok.Data;

@Data
public class RsvpRequest {
    private Integer eventId;
    private String status;
    private Integer plusOnes;
    private String comments;
}
