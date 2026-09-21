package atm;

public class ATMException extends Exception {
    private final String message;

    public ATMException(String message) {
        super(message);
        this.message = message;
    }
     @Override
    public String getMessage() {
        return message;
    }
    
}
