package com.bteam.fantasychess_server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultTest {

    @Test
    void asSuccess() {
        var success = Result.asSuccess("");
        assertTrue(success.isSuccess());
    }

    @Test
    void asFailure() {
        var failure = Result.asFailure(new Exception());
        assertFalse(failure.isSuccess());
    }

    @Test
    void isSuccess() {
        var success = new Result<>("", null);
        var failure = new Result<>(null, new Exception());

        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());
    }
}