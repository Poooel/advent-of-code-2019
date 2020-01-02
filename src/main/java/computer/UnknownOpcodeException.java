package computer;

public class UnknownOpcodeException extends RuntimeException {
    public UnknownOpcodeException(String errorMessage) {
        super(errorMessage);
    }
}
