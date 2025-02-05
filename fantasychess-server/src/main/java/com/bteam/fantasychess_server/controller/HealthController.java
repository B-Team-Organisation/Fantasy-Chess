package com.bteam.fantasychess_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    public static final String STATUS_OK = "Ok";

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok(STATUS_OK);
    }
}
