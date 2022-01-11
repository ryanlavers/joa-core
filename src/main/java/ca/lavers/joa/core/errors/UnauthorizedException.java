package ca.lavers.joa.core.errors;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException() {
        super(401, "Unauthorized");
    }
}
