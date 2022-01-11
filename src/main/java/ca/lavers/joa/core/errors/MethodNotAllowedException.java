package ca.lavers.joa.core.errors;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException() {
        super(405, "Method Not Allowed");
    }

    public MethodNotAllowedException(String detail) {
        super(405, "Method Not Allowed: " + detail);
    }
}
