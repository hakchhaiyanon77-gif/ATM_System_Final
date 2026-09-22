# ATM System

A Java console ATM project by:

- Hak Chhaiya Non
- Phoung Monyrak

## Features

- Register a savings or checking account
- Log in using a card/account number and PIN
- Lock after three consecutive wrong PIN attempts
- Check balance
- Deposit and withdraw money
- Transfer money between accounts
- View transaction history
- Change PIN
- Apply savings interest
- Use a checking account overdraft
- Log out and exit

All data stays in memory. Closing the program resets accounts, balances, PIN changes and history. There is no database or account file storage.

## Requirements

Java JDK 17 or newer.

## How to Run

Open a terminal in the project folder.

Compile all Java files:

```bash
javac -d out *.java
```

Run the program:

```bash
java -cp out Main
```

These commands use our flat folder layout without `package atm;`. Save and compile again after changing the code.

## Sample Accounts

| Card/account number | PIN | Type | Starting balance |
|---|---|---|---|
| 1001 | 1234 | Savings | $1,000 |
| 1002 | 2345 | Checking | $2,000 |

The card number is the same as the account number in this simulation.

## Register an Account

Choose **2. Register** from the main menu.

Enter:

1. Account type: `1` for Savings or `2` for Checking
2. A new account number containing 4–12 digits
3. A four-digit PIN
4. The same PIN again to confirm
5. An opening balance

Account numbers must be unique. Savings accounts require an opening balance of at least $10. Checking accounts can start at $0.

After registration, choose **1. Login** and enter the new account details. Registered accounts disappear when the program closes.

## Account Rules

- Savings accounts must keep at least $10.
- Savings accounts have a $500 daily outgoing limit.
- Savings interest is 1%, applied manually once per calendar month during a run.
- Checking accounts allow an overdraft of up to $200.
- Checking accounts have a $1,000 daily outgoing limit.
- Withdrawals and outgoing transfers share the daily limit.
- Transaction amounts must be positive and use at most two decimal places.
- Wrong old PINs during a PIN change also count toward locking.
- There is no admin menu or unlock option in this version.

## Project Structure

All twelve Java files are directly inside the project folder.

| File | Purpose |
|---|---|
| Main.java | Starts the program |
| ATM.java | Menus, registration, login and user interaction |
| Account.java | Shared account data and banking operations |
| SavingsAccount.java | Savings rules and interest |
| CheckingAccount.java | Checking rules and overdraft |
| Card.java | PIN checks and failed-attempt tracking |
| Transaction.java | Stores an activity record |
| TransactionType.java | Defines activity types |
| ATMException.java | Base custom exception |
| InvalidPinException.java | PIN-related errors |
| InsufficientFundsException.java | Insufficient available funds |
| AccountNotFoundException.java | Missing account or card errors |

The `out` folder is generated when compiling and contains `.class` files. It does not need to be uploaded to GitHub.

## OOP Concepts

- **Encapsulation:** Fields are private and accessed through methods.
- **Abstraction:** `Account` is an abstract class.
- **Inheritance:** `SavingsAccount` and `CheckingAccount` extend `Account`.
- **Polymorphism:** Account types provide different available-funds rules.
- **Exception handling:** Custom exceptions provide readable error messages.

## Team Contributions

### Hak Chhaiya Non

Worked on:

- Account.java
- CheckingAccount.java
- SavingsAccount.java
- ATM.java
- ATMException.java
- Main.java

Also set up the GitHub repository, handled the README and report, and made small corrections while combining the files.

### Phoung Monyrak

Worked on:

- Card.java
- Transaction.java
- TransactionType.java
- InvalidPinException.java
- InsufficientFundsException.java
- AccountNotFoundException.java

Also drew the UML diagram in draw.io and collaborated on the GitHub repository.

Both members manually tested the ATM and learned how it works.

## Limitations

This is a classroom simulation. Data resets when the program closes, and PINs are visible while typing. It is not connected to a real bank.

## AI Assistance

AI helped generate the initial implementation and, explain code, troubleshoot errors and draft documentation. We divided the files between us for review, changes and further work.
