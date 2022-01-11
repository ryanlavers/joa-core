package ca.lavers.joa.core.errors;

public class BadRequestException extends HttpException {
    public BadRequestException() {
        super(400, "Bad Request");
    }

    public BadRequestException(String detail) {
        super(400, "Bad Request: " + detail);
    }
}
