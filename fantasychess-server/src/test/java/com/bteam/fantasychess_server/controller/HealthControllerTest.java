package com.bteam.fantasychess_server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }

    @Test
    void getHealth() {
        assertEquals(HealthController.STATUS_OK, healthController.getHealth().getBody());
    }
}