package com.wedding.backend.service;

import com.wedding.backend.dto.SiteConfigDTO;
import com.wedding.backend.model.SiteConfig;
import com.wedding.backend.repository.SiteConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SiteConfigServiceImpl implements SiteConfigService {

    @Autowired
    private SiteConfigRepository siteConfigRepository;

    private static final Integer CONFIG_ID = 1;

    @Override
    @Transactional(readOnly = true)
    public SiteConfigDTO getSiteConfig() {
        SiteConfig config = siteConfigRepository.findById(CONFIG_ID)
                .orElse(new SiteConfig()); // Return default/empty config if not found
        return toDTO(config);
    }

    @Override
    @Transactional
    public SiteConfigDTO updateSiteConfig(SiteConfigDTO siteConfigDTO) {
        SiteConfig config = siteConfigRepository.findById(CONFIG_ID)
                .orElse(new SiteConfig());

        config.setSiteTitle(siteConfigDTO.getSiteTitle());
        config.setWelcomeMessage(siteConfigDTO.getWelcomeMessage());
        config.setGalleryEnabled(siteConfigDTO.isGalleryEnabled());
        config.setRsvpEnabled(siteConfigDTO.isRsvpEnabled());
        config.setRegistryInfo(siteConfigDTO.getRegistryInfo());

        SiteConfig updatedConfig = siteConfigRepository.save(config);
        return toDTO(updatedConfig);
    }

    private SiteConfigDTO toDTO(SiteConfig config) {
        return new SiteConfigDTO(
                config.getSiteTitle(),
                config.getWelcomeMessage(),
                config.isGalleryEnabled(),
                config.isRsvpEnabled(),
                config.getRegistryInfo()
        );
    }
}
