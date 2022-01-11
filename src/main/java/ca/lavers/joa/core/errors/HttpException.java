package ca.lavers.joa.core.errors;

/**
 * Base class for exceptions that can indicate the HTTP status code
 * and error message to be returned to the client
 *
 * Middleware can throw these exceptions as a convenience for indicating
 * common errors, such as {@link NotFoundException} (404 Not Found)
 *
 * Requires middleware to convert the exception to an error response (such
 * as ErrorHandler from joa-middleware) to be installed in the chain above
 * any middleware that wishes to throw these exceptions, otherwise the
 * default 500 Internal Server Error will be returned for all uncaught
 * exceptions.
 */
public class HttpException extends RuntimeException {

    private final int status;
    private final String message;

    public HttpException(int status, String message) {
        super("HTTP Error: " + status + " - " + message);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getReturnedMessage() {
        return message;
    }
}
