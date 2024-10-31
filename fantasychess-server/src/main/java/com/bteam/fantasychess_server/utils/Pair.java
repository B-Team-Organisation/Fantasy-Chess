package com.bteam.fantasychess_server.utils;

/**
 * A simple helper Class to allow for Pairs
 *
 * @param <T> Type of the first Object in the Pair
 * @param <U> type of the second Object in the Pair
 * @author Marc
 */
public class Pair<T, U> {
    T first;
    U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }
}
