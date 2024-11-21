package com.bteam.common.utils;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Event class providing methods to provide Listeners and Invoke all listeners
 *
 * @param <T> Consumer type, which gets parsed down to all Listeners
 * @author Marc
 */
public class Event<T> {
    ArrayList<Consumer<T>> listeners = new ArrayList<>();
    Consumer<Exception> exceptionHandler = System.out::println;

    public Event() {
    }

    public Event(Consumer<Exception> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Clear all listeners from this event
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * Invoke all Listeners with the event bound error handler
     *
     * @param t Object Provided to listeners
     */
    public void invoke(T t) {
        invoke(t, exceptionHandler);
    }


    /**
     * Invoke all Listeners with a custom error callback
     *
     * @param t Object Provided to listeners
     */
    public void invoke(T t, Consumer<Exception> errorCallback) {
        for (var listener :
                listeners) {
            try {
                listener.accept(t);
            } catch (Exception e) {
                errorCallback.accept(e);
            }
        }
    }
}
