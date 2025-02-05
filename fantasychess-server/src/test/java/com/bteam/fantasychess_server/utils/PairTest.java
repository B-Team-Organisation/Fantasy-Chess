package com.bteam.fantasychess_server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PairTest {

    private final static String FIRST = "FIRST";
    private final static String SECOND = "SECOND";
    private final static String THIRD = "THIRD";

    @Test
    void getFirst() {
        var pair = new Pair<>(FIRST, SECOND);
        assertEquals(FIRST, pair.getFirst());
    }

    @Test
    void setFirst() {
        var pair = new Pair<>(FIRST, SECOND);
        pair.setFirst(THIRD);
        assertEquals(THIRD, pair.getFirst());
    }

    @Test
    void getSecond() {
        var pair = new Pair<>(FIRST, SECOND);
        assertEquals(SECOND, pair.getSecond());
    }

    @Test
    void setSecond() {
        var pair = new Pair<>(FIRST, SECOND);
        pair.setSecond(THIRD);
        assertEquals(THIRD, pair.getSecond());
    }
}