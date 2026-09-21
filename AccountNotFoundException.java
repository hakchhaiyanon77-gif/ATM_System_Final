
public class AccountNotFoundException extends ATMException {
    private final String message;

    public AccountNotFoundException(String message) {
        super(message);
        this.message = message;

    }
    @Override 
    public String getMessage() { return message; }
}