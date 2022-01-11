package ca.lavers.joa.core.errors;

public class TooManyRequestsException extends HttpException {
    public TooManyRequestsException() {
        super(429, "Too Many Requests");
    }
}
