package com.wedding.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteConfigDTO {
    private String siteTitle;
    private String welcomeMessage;
    private boolean galleryEnabled;
    private boolean rsvpEnabled;
    private String registryInfo;
}
