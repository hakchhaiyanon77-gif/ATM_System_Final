# ATM System

A classic Java console project by:

- Hak Chhaiya Non
- Phoung Monyrak

## Features

- Login using a card/account number and PIN
- Lock after three wrong PIN attempts
- Check balance
- Deposit and withdraw money
- Transfer money between accounts
- View transaction history
- Change PIN
- Apply savings interest
- Checking account overdraft
- Logout and exit

All data stays in memory and resets when the program closes.

## Requirements

Java JDK 17 or newer.

## How to Run

Open a terminal in the project folder.

Compile:

```bash
javac -d out *.java
```

Run:

```bash
java -cp out Main
```

These commands use the flat project layout without `package atm;`.

## Sample Accounts

| Account Number | PIN | Type | Starting Balance |
|---|---|---|---|
| 1001 | 1234 | Savings | $1,000 |
| 1002 | 2345 | Checking | $2,000 |

## Account Rules

- Savings: $10 minimum balance and $500 daily outgoing limit.
- Savings interest: 1%, applied manually once per calendar month during a run.
- Checking: $200 overdraft and $1,000 daily outgoing limit.
- Withdrawals and outgoing transfers share the daily limit.

## OOP Concepts

- Encapsulation: private fields and controlled methods.
- Abstraction: the abstract `Account` class.
- Inheritance: savings and checking accounts extend `Account`.
- Polymorphism: account types use different available-funds rules.
- Exception handling: custom exceptions display understandable error messages.

## Contributions

- Hak Chhaiya Non: [Add your actual contribution]
- Phoung Monyrak: [Add your actual contribution]

## AI Assistance

Codex helped generate the initial code and documentation.
