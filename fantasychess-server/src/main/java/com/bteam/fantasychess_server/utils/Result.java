package com.bteam.fantasychess_server.utils;

/**
 * A result class that wraps the {@link Pair} class
 *
 * @param <T> Type of the resulting success data
 */
public class Result<T> extends Pair<T, Exception> {

    public Result(T first, Exception second) {
        super(first, second);
    }

    /**
     * @param value Value of the result object
     * @param <T>   type of the result Object
     * @return new Success result
     */
    public static <T> Result<T> asSuccess(T value) {
        return new Result<>(value, null);
    }

    /**
     * @param e   Exception that lead to the failure
     * @param <T> type of the result Object
     * @return new Success result
     */
    public static <T> Result<T> asFailure(Exception e) {
        return new Result<>(null, e);
    }

    public boolean isSuccess() {
        return second == null;
    }
}
