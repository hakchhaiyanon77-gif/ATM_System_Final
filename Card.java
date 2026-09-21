
public class Card {
    private final String cardNumber;
    private int attempts;
    private boolean locked;
    private final Account account;

    public Card(String cardNumber, Account account) throws ATMException {
        if (cardNumber == null || !cardNumber.matches("[0-9]{4,16}") || account == null) {
            throw new ATMException("A card needs a valid number and account.");
        }
        this.cardNumber = cardNumber;
        this.account = account;
    }

    public String getCardNumber() { return cardNumber; }
    public Account getAccount() { return account; }

    public boolean validatePIN(String pin) {
        if (locked) return false;
        if (!account.verifyPIN(pin)) {
            incrementAttempts();
            return false;
        }
        resetAttempts();
        return true;
    }

    public void incrementAttempts() {
        if (attempts < 3) attempts++;
        if (attempts >= 3) locked = true;
    }

    public void resetAttempts() { if (!locked) attempts = 0; }
    public boolean isLocked() {
         return locked; }
}
