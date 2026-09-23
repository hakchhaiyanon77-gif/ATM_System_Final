# ATM System

A classic Java console project by:

- Hak Chhaiya Non
- Phoung Monyrak

## Features

- Register with an automatically generated account/card number
- View card details after login
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

## Registration and Card Details

Choose **2. Register**, select **1. Savings** or **2. Checking**, then enter your full name and phone number. A name must contain 1-80 characters. A phone number must contain 8-15 digits, with an optional leading `+`; leading zeroes are preserved. This checks the format only, not phone ownership.

Savings registration asks for a simulated opening deposit of at least $10. Savings has a $500 daily outgoing limit, $10 minimum balance and 1% monthly interest. Checking starts at $0 with a $1,000 daily outgoing limit and the existing $200 overdraft allowance.

The program assigns the next unused account/card number starting at `1003`, and generates a random four-digit PIN. Registration displays the PIN once; remember it and use Change PIN after login if needed. These are simplified classroom rules, not a real bank registration process.

In this simulation, the card number is the same as its linked account number. Use the generated number and PIN to log in. Choose **8. Card details** to see your name, phone number, card number, account number, account type and status. Card details never displays the PIN. The original sample accounts show a sample name and no phone number.

The original sample savings account is still available for interest demonstrations. Registered accounts, contact details, assigned numbers and PINs reset when the program closes because storage is memory-only. Different accounts may share a phone number; this version does not restrict accounts per person.

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
