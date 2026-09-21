
public class InsufficientFundsException extends ATMException {
    private final String message;
    public InsufficientFundsException(String message) {
        super(message);
        this.message = message;
    }
    @Override 
    public String getMessage() { 
        return message;
    }
}