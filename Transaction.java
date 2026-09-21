package atm;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime dateTime;
    private final double balanceAfter;
}