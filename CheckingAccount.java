
public class CheckingAccount extends Account {
    private final double overdraftLimit;

    public CheckingAccount(String number, String pin, double balance,
            double dailyLimit, double overdraftLimit) throws ATMException {
        super(number, pin, balance, dailyLimit);
        validateMoney(overdraftLimit, true);
        this.overdraftLimit = overdraftLimit;
    }

    public double getAvailableBalance() { return roundMoney(getBalance() + overdraftLimit); }

    @Override
    protected double availableFunds() { return getAvailableBalance(); }

    @Override
    public void withdraw(double amount) throws ATMException { super.withdraw(amount); }

    @Override
    public String getAccountType() { 
        return "Checking"; }
}
