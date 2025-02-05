package com.bteam.fantasychess_server.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    static final Consumer<Void> TEST_CONSUMER = c -> System.out.println();
    static final String TEST_ARGUMENT = "TEST_ARGUMENT";

    @Test
    void addListener() {
        Event<Void> event = new Event<>();
        event.addListener(TEST_CONSUMER);
        assertTrue(event.listeners.contains(TEST_CONSUMER));
    }

    @Test
    void removeListener() {
        Event<Void> event = new Event<>();
        event.addListener(TEST_CONSUMER);
        assertTrue(event.listeners.contains(TEST_CONSUMER));
        event.removeListener(TEST_CONSUMER);
        assertFalse(event.listeners.contains(TEST_CONSUMER));
    }

    @Test
    void clear() {
        Event<Void> event = new Event<>();
        for (int i = 0; i < 3; i++) {
            event.addListener(TEST_CONSUMER);
        }
        assertEquals(3, event.listeners.size());
        event.clear();
        assertEquals(0, event.listeners.size());
    }

    @Test
    void invoke() {
        Event<String> event = new Event<>();
        AtomicInteger calls = new AtomicInteger();

        event.addListener(c -> {
            assertEquals(TEST_ARGUMENT, c);
            calls.getAndIncrement();
        });
        event.invoke(TEST_ARGUMENT);

        assertEquals(1, calls.get());
    }

    @Test
    void invokeWithError() {
        Event<String> event = new Event<>();
        AtomicInteger calls = new AtomicInteger();
        var exception = new RuntimeException("TEST_EXCEPTION");

        event.addListener(c -> {
            throw exception;
        });

        event.invoke(TEST_ARGUMENT, e -> {
            assertEquals(exception, e);
            calls.getAndIncrement();
        });

        assertEquals(1, calls.get());
    }
}