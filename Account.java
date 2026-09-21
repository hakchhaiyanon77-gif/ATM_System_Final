
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private final String accountNumber;
    private String pin;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();
    private final double dailyWithdrawalLimit;
    private double withdrawnToday;
    private LocalDate withdrawalDate = LocalDate.now();
    private static final double MAX_BALANCE = 1_000_000;

    public Account(String accountNumber, String pin, double balance,
                   double dailyWithdrawalLimit) throws ATMException {
        if (accountNumber == null || !accountNumber.matches("[0-9]{4,12}")) {
            throw new ATMException("Account number must have 4 to 12 digits.");
        }
        checkPin(pin);
        validateMoney(balance, true);
        validateMoney(dailyWithdrawalLimit, false);
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public boolean verifyPIN(String pin) { return this.pin.equals(pin); }
    public abstract String getAccountType();

    protected static double roundMoney(double amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static void validateMoney(double amount, boolean allowZero) throws ATMException {
        if (!Double.isFinite(amount) || amount < 0 || (!allowZero && amount == 0)
                || amount > MAX_BALANCE) {
            throw new ATMException("Amount must be " + (allowZero ? "zero or above" : "above zero")
                    + " and at most $1,000,000.");
        }
        if (BigDecimal.valueOf(amount).stripTrailingZeros().scale() > 2) {
            throw new ATMException("Use at most two decimal places.");
        }
    }

    private void checkPin(String pin) throws InvalidPinException {
        if (pin == null || !pin.matches("[0-9]{4}")) {
            throw new InvalidPinException("PIN must contain exactly 4 digits.");
        }
    }

    protected double availableFunds() { return balance; }

    private void checkWithdrawal(double amount) throws ATMException {
        validateMoney(amount, false);
        if (!withdrawalDate.equals(LocalDate.now())) {
            withdrawalDate = LocalDate.now();
            withdrawnToday = 0;
        }
        if (amount > availableFunds()) {
            throw new InsufficientFundsException("Insufficient available funds for this account.");
        }
        if (roundMoney(withdrawnToday + amount) > dailyWithdrawalLimit) {
            throw new ATMException("Daily withdrawal/transfer limit exceeded: $"
                    + String.format(java.util.Locale.US, "%.2f", dailyWithdrawalLimit));
        }
    }

    private void checkDeposit(double amount) throws ATMException {
        validateMoney(amount, false);
        if (roundMoney(balance + amount) > MAX_BALANCE) {
            throw new ATMException("Receiving balance would exceed $1,000,000.");
        }
    }

    public void deposit(double amount) throws ATMException {
        checkDeposit(amount);
        balance = roundMoney(balance + amount);
        addTransaction(new Transaction(TransactionType.DEPOSIT, amount, balance));
    }

    public void withdraw(double amount) throws ATMException {
        checkWithdrawal(amount);
        balance = roundMoney(balance - amount);
        withdrawnToday = roundMoney(withdrawnToday + amount);
        addTransaction(new Transaction(TransactionType.WITHDRAW, amount, balance));
    }

    public void transfer(Account to, double amount) throws ATMException {
        if (to == null) throw new AccountNotFoundException("Receiving account not found.");
        if (accountNumber.equals(to.accountNumber)) {
            throw new ATMException("Choose another account for the transfer.");
        }
        checkWithdrawal(amount);
        to.checkDeposit(amount);
    
        balance = roundMoney(balance - amount);
        to.balance = roundMoney(to.balance + amount);
        withdrawnToday = roundMoney(withdrawnToday + amount);
        addTransaction(new Transaction(TransactionType.TRANSFER, -amount, balance));
        to.addTransaction(new Transaction(TransactionType.TRANSFER, amount, to.balance));
    }

    public void changePIN(String oldPin, String newPin) throws InvalidPinException {
        if (!verifyPIN(oldPin)) throw new InvalidPinException("Old PIN is incorrect.");
        checkPin(newPin);
        pin = newPin;
        addTransaction(new Transaction(TransactionType.PIN_CHANGE, 0, balance));
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) transactions.add(transaction);
    }

    public List<Transaction> getTransaction() { return new ArrayList<>(transactions); }
}
