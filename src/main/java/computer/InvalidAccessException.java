package computer;

public class InvalidAccessException extends RuntimeException {
    public InvalidAccessException(String errorMessage) {
        super(errorMessage);
    }
}
