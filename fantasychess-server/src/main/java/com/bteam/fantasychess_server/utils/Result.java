package com.bteam.fantasychess_server.utils;

public class Result<T> extends Pair<T, Exception> {

    public Result(T first, Exception second) {
        super(first, second);
    }

    public static <T> Result<T> asSuccess(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> asFailure(Exception e) {
        return new Result<>(null, e);
    }

    public boolean isSuccess() {
        return second == null;
    }
}
