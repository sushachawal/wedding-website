package com.wedding.backend.controller;

import com.wedding.backend.dto.SiteConfigDTO;
import com.wedding.backend.service.SiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/config")
public class AdminController {

    @Autowired
    private SiteConfigService siteConfigService;

    @GetMapping
    public ResponseEntity<SiteConfigDTO> getSiteConfig() {
        SiteConfigDTO config = siteConfigService.getSiteConfig();
        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<SiteConfigDTO> updateSiteConfig(@RequestBody SiteConfigDTO siteConfigDTO) {
        SiteConfigDTO updatedConfig = siteConfigService.updateSiteConfig(siteConfigDTO);
        return ResponseEntity.ok(updatedConfig);
    }
}
