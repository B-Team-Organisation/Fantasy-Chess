package com.bteam.common.utils;

import java.util.Objects;

/**
 * A simple helper Class to allow for Pairs
 *
 * @param <T> Type of the first Object in the Pair
 * @param <U> type of the second Object in the Pair
 * @author Marc, Jacinto
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

    @Override
    public String toString() {
        return "[First: " + first.toString() + "; Second: " + second.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Pair && ((Pair<?, ?>)o).first.equals(first) && ((Pair<?, ?>)o).second.equals(second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
