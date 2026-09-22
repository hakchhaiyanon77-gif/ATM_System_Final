import java.util.HashMap;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ATM {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private Account currentAccount;
    private boolean isAuthenticated;
    private final HashMap<String, Card> cards = new HashMap<>();
    private Card currentCard;
    private final Scanner input = new Scanner(System.in);

    public ATM() {
        try {
            Account savings = new SavingsAccount(
                    "1001", "1234", 1000, 500, 0.01, 10);

            Account checking = new CheckingAccount(
                    "1002", "2345", 2000, 1000, 200);

            accounts.put("1001", savings);
            accounts.put("1002", checking);

            cards.put("1001", new Card("1001", savings));
            cards.put("1002", new Card("1002", checking));
        } catch (ATMException e) {
            throw new IllegalStateException("Invalid sample account setup.", e);
        }
    }

    private String read(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private double readAmount(String prompt) throws ATMException {
        String value = read(prompt);

        if (!value.matches("[0-9]{1,7}(\\.[0-9]{1,2})?")) {
            throw new ATMException("Enter an amount like 20 or 20.50.");
        }

        double amount = Double.parseDouble(value);
        Account.validateMoney(amount, false);
        return amount;
    }

    public void register() throws ATMException {
        System.out.println("\n--- Register Account ---");

        String type = read("Type (1 Savings, 2 Checking): ");

        if (!type.equals("1") && !type.equals("2")) {
            throw new ATMException("Choose 1 or 2 for account type.");
        }

        String number = read("New account number (4-12 digits): ");

        if (!number.matches("[0-9]{4,12}")) {
            throw new ATMException(
                    "Account number must have 4 to 12 digits.");
        }

        if (accounts.containsKey(number) || cards.containsKey(number)) {
            throw new ATMException("This account number already exists.");
        }

        String pin = read("New 4-digit PIN: ");

        if (!pin.matches("[0-9]{4}")) {
            throw new InvalidPinException(
                    "PIN must contain exactly 4 digits.");
        }

        String confirmPin = read("Confirm PIN: ");

        if (!pin.equals(confirmPin)) {
            throw new InvalidPinException("The PINs do not match.");
        }

        String balanceText = read("Opening balance: ");

        if (!balanceText.matches("[0-9]{1,7}(\\.[0-9]{1,2})?")) {
            throw new ATMException("Enter a balance like 100 or 100.50.");
        }

        double balance = Double.parseDouble(balanceText);
        Account.validateMoney(balance, true);

        Account account;

        if (type.equals("1")) {
            account = new SavingsAccount(
                    number, pin, balance, 500, 0.01, 10);
        } else {
            account = new CheckingAccount(
                    number, pin, balance, 1000, 200);
        }

        Card card = new Card(number, account);
        accounts.put(number, account);
        cards.put(number, card);

        System.out.println("Account created successfully.");
        System.out.println("Your card/account number is: " + number);
        System.out.println("You can now log in.");
    }

    public boolean login(String cardNumber, String pin) throws ATMException {
        logout();

        Card card = cards.get(cardNumber);

        if (card == null) {
            throw new AccountNotFoundException(
                    "Card/account number not found.");
        }

        if (!card.validatePIN(pin)) {
            throw new InvalidPinException(card.isLocked()
                    ? "Card/account locked after 3 wrong PINs."
                    : "Wrong PIN. Try again.");
        }

        currentCard = card;
        currentAccount = card.getAccount();
        isAuthenticated = true;
        return true;
    }

    private void requireLogin() throws ATMException {
        if (!isAuthenticated || currentCard == null || currentCard.isLocked()) {
            throw new ATMException("Please log in first.");
        }
    }

    public void checkBalance() throws ATMException {
        requireLogin();

        System.out.printf(
                Locale.US, "Balance: $%.2f%n", currentAccount.getBalance());

        if (currentAccount instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) currentAccount;

            System.out.printf(
                    Locale.US,
                    "Available including overdraft: $%.2f%n",
                    checking.getAvailableBalance());
        }

        currentAccount.addTransaction(new Transaction(
                TransactionType.BALANCE_INQUIRY,
                0,
                currentAccount.getBalance()));
    }

    public void deposit(double amount) throws ATMException {
        requireLogin();
        currentAccount.deposit(amount);
        System.out.println("Deposit successful.");
    }

    public void withdraw(double amount) throws ATMException {
        requireLogin();
        currentAccount.withdraw(amount);
        System.out.println("Withdrawal successful.");
    }

    public void transfer(String accountNo, double amount) throws ATMException {
        requireLogin();

        Account receiver = accounts.get(accountNo);

        if (receiver == null) {
            throw new AccountNotFoundException(
                    "Receiving account not found.");
        }

        if (cards.get(accountNo).isLocked()) {
            throw new ATMException("Receiving account is locked.");
        }

        currentAccount.transfer(receiver, amount);
        System.out.println("Transfer successful.");
    }

    public void showHistory() throws ATMException {
        requireLogin();

        if (currentAccount.getTransaction().isEmpty()) {
            System.out.println("No transactions yet.");
        }

        for (Transaction t : currentAccount.getTransaction()) {
            System.out.printf(
                    Locale.US,
                    "%s | %s | $%.2f | Balance: $%.2f%n",
                    t.getDateTime().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    t.getType(),
                    t.getAmount(),
                    t.getBalanceAfter());
        }
    }

    public void changePIN(String oldPin, String newPin) throws ATMException {
        requireLogin();

        if (!currentCard.validatePIN(oldPin)) {
            boolean locked = currentCard.isLocked();

            if (locked) {
                logout();
            }

            throw new InvalidPinException(locked
                    ? "Card/account locked. Session ended."
                    : "Old PIN is incorrect.");
        }

        currentAccount.changePIN(oldPin, newPin);
        System.out.println("PIN changed.");
    }

    public void logout() {
        currentAccount = null;
        currentCard = null;
        isAuthenticated = false;
    }

    public void showMenu() {
        System.out.println(
                "\n1. Balance"
                + "\n2. Deposit"
                + "\n3. Withdraw"
                + "\n4. Transfer"
                + "\n5. History"
                + "\n6. Change PIN"
                + "\n7. Apply monthly savings interest"
                + "\n0. Logout");
    }

    public void start() {
        try {
            while (true) {
                System.out.println(
                        "\n=== ATM System ==="
                        + "\n1. Login"
                        + "\n2. Register"
                        + "\n0. Exit");

                String choice = read("Choose: ");

                if (choice.equals("0")) {
                    System.out.println("Goodbye.");
                    return;
                }

                if (choice.equals("2")) {
                    try {
                        register();
                    } catch (ATMException e) {
                        System.out.println(e.getMessage());
                    }
                    continue;
                }

                if (!choice.equals("1")) {
                    System.out.println("Choose 1, 2 or 0.");
                    continue;
                }

                try {
                    login(read("Card/account number: "), read("PIN: "));

                    System.out.println(
                            "Welcome, account "
                            + currentAccount.getAccountNumber()
                            + " (" + currentAccount.getAccountType() + ").");

                    while (isAuthenticated) {
                        showMenu();

                        try {
                            switch (read("Choose: ")) {
                                case "1":
                                    checkBalance();
                                    break;

                                case "2":
                                    deposit(readAmount("Deposit amount: "));
                                    break;

                                case "3":
                                    withdraw(readAmount("Withdrawal amount: "));
                                    break;

                                case "4":
                                    String target = read("Receiver account number: ");
                                    transfer(target, readAmount("Transfer amount: "));
                                    break;

                                case "5":
                                    showHistory();
                                    break;

                                case "6":
                                    changePIN(
                                            read("Old PIN: "),
                                            read("New PIN: "));
                                    break;

                                case "7":
                                    if (!(currentAccount instanceof SavingsAccount)) {
                                        throw new ATMException(
                                                "Interest is only available for savings.");
                                    }

                                    ((SavingsAccount) currentAccount).applyInterest();
                                    System.out.println("Monthly interest applied (1%).");
                                    break;

                                case "0":
                                    logout();
                                    System.out.println("Logged out.");
                                    break;

                                default:
                                    System.out.println("Choose a number from the menu.");
                            }
                        } catch (ATMException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (ATMException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            logout();
            System.out.println("\nInput closed. Goodbye.");
        }
    }
}