package Exceptions;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String methodName) {
        super(String.format("Method %s not implemented", methodName));
    }
}
