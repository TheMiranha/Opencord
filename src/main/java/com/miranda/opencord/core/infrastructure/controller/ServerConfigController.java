package com.miranda.opencord.core.infrastructure.controller;

import com.miranda.opencord.core.infrastructure.controller.dto.ServerConfigOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ServerConfigController {

    @Value("${livekit.url:ws://localhost:7880}")
    private String livekitUrl;

    @GetMapping
    public ResponseEntity<ServerConfigOutput> getConfig() {
        return ResponseEntity.ok(new ServerConfigOutput(livekitUrl, "1.0.0"));
    }
}
