
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime dateTime;
    private final double balanceAfter;

    public Transaction(TransactionType type, double amount, double balanceAfter) {
    this.id = UUID.randomUUID().toString();
    this.type = type;
    this.amount = amount;
    this.dateTime = LocalDateTime.now();
    this.balanceAfter = balanceAfter;
    }
    
    public String getId() {
    return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
}