
public class InvalidPinException extends ATMException {
    private final String message;

    public InvalidPinException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() { return message; }
}
