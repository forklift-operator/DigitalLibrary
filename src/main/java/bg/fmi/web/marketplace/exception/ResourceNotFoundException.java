package bg.fmi.web.marketplace.exception;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String className, Object id) {
        super(className + " with id " + id + " not found");
    }
}