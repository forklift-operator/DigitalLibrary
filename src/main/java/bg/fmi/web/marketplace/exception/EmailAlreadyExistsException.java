package bg.fmi.web.marketplace.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("User with this email already exists: " + email);
    }
}
