package com.wedding.backend.service;

import com.wedding.backend.dto.SiteConfigDTO;
import com.wedding.backend.model.SiteConfig;
import com.wedding.backend.repository.SiteConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiteConfigServiceImplTest {

    @Mock
    private SiteConfigRepository siteConfigRepository;

    @InjectMocks
    private SiteConfigServiceImpl siteConfigService;

    @Test
    void getSiteConfig_shouldReturnStoredValues() {
        SiteConfig config = new SiteConfig();
        config.setSiteTitle("Title");
        config.setWelcomeMessage("Welcome");
        config.setGalleryEnabled(true);
        config.setRsvpEnabled(false);
        config.setRegistryInfo("Info");

        when(siteConfigRepository.findById(1)).thenReturn(Optional.of(config));

        SiteConfigDTO dto = siteConfigService.getSiteConfig();

        assertEquals("Title", dto.getSiteTitle());
        assertEquals("Welcome", dto.getWelcomeMessage());
        assertTrue(dto.isGalleryEnabled());
        assertFalse(dto.isRsvpEnabled());
        assertEquals("Info", dto.getRegistryInfo());
    }

    @Test
    void updateSiteConfig_shouldPersistChanges() {
        SiteConfig existing = new SiteConfig();
        when(siteConfigRepository.findById(1)).thenReturn(Optional.of(existing));
        when(siteConfigRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SiteConfigDTO update = new SiteConfigDTO("New", "Msg", true, true, "Reg");
        SiteConfigDTO result = siteConfigService.updateSiteConfig(update);

        assertEquals("New", existing.getSiteTitle());
        assertEquals("Msg", existing.getWelcomeMessage());
        verify(siteConfigRepository).save(existing);
        assertEquals("Reg", result.getRegistryInfo());
    }
}
