package ca.lavers.joa.core.errors;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(404, "Not Found");
    }

    public NotFoundException(String detail) {
        super(404, "Not Found: " + detail);
    }
}
