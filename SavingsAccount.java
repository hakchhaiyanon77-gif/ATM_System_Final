
import java.time.YearMonth;

public class SavingsAccount extends Account {
    private final double interestRate;
    private final double minimumBalance;
    private YearMonth lastInterestMonth;

    public SavingsAccount(String number, String pin, double balance,
            double dailyLimit, double interestRate, double minimumBalance) throws ATMException {
        super(number, pin, balance, dailyLimit);
        validateMoney(minimumBalance, true);
        if (balance < minimumBalance) throw new ATMException("Opening balance is below the minimum.");
        if (!Double.isFinite(interestRate) || interestRate < 0 || interestRate > 1) {
            throw new ATMException("Interest rate must be between 0 and 1.");
        }
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    @Override
    protected double availableFunds() { return roundMoney(getBalance() - minimumBalance); }

    @Override
    public void withdraw(double amount) throws ATMException { super.withdraw(amount); }

    public void applyInterest() throws ATMException {
        YearMonth month = YearMonth.now();
        if (month.equals(lastInterestMonth)) {
            throw new ATMException("Interest has already been applied this month.");
        }
        double interest = roundMoney(getBalance() * interestRate);
        if (interest > 0) deposit(interest);
        lastInterestMonth = month;
    }

    @Override
    public String getAccountType() { return "Savings"; }
}
