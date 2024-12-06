package com.bteam.common.utils;

import java.util.List;
import java.util.Objects;

public class ListNoOrder<E> {
    private final List<E> list;

    public ListNoOrder(List<E> list) {
        this.list = list;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListNoOrder)) return false;
        ListNoOrder<?> other = (ListNoOrder<?>) o;

        return list.containsAll(other.list) && other.list.containsAll(list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
