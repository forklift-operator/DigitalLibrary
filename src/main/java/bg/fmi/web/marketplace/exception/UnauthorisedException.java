package bg.fmi.web.marketplace.exception;

public class UnauthorisedException extends RuntimeException {
    public UnauthorisedException() {
        super("Unauthorized");
    }
}
