package atm;

public class AccountNotFoundException extends ATMException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}