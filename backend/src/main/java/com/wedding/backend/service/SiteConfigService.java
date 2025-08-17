package com.wedding.backend.service;

import com.wedding.backend.dto.SiteConfigDTO;

public interface SiteConfigService {
    SiteConfigDTO getSiteConfig();
    SiteConfigDTO updateSiteConfig(SiteConfigDTO siteConfigDTO);
}
