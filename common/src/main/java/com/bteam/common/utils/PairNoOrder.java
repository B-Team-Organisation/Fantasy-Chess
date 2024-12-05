package com.bteam.common.utils;

import java.util.Objects;

/**
 * {@link Pair} But equals doesn't account for order
 *
 * @param <T> Type of the first Object in the Pair
 * @param <U> type of the second Object in the Pair
 * @author Jacinto, Marc
 */
public class PairNoOrder<T, U> {
    T first;
    U second;

    public PairNoOrder(T first, U second) {
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
        if (this == o) return true;
        if (!(o instanceof PairNoOrder)) return false;
        PairNoOrder<?, ?> other = (PairNoOrder<?, ?>) o;
        return (other.getFirst().equals(first) && other.getSecond().equals(second))
                || (other.getFirst().equals(second) && other.getSecond().equals(first));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
