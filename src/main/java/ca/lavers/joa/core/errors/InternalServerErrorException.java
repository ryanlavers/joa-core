package ca.lavers.joa.core.errors;

public class InternalServerErrorException extends HttpException {

    // TODO - Use the cause that Throwable already has
    private Throwable cause;

    // TODO: Allow a message to be logged; maybe require a cause?
    public InternalServerErrorException() {
        super(500, "Internal Server Error");
    }

    public InternalServerErrorException(Throwable cause) {
        this();
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
