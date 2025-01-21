package com.bteam.common.exceptions;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String methodName) {
        super("Method " + methodName + " not implemented");
    }
}
