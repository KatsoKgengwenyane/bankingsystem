package model;

public class ChequeAccount extends Account {

    // Constructor for new account creation
    public ChequeAccount(int customerId, String accountNumber, String branch) {
        super(customerId, accountNumber, branch, "ChequeAccount");
    }

    // Constructor with initial balance
    public ChequeAccount(int customerId, String accountNumber, String branch, double initialBalance) {
        super(customerId, accountNumber, branch, "ChequeAccount");
        if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative.");
        this.balance = initialBalance;
    }

    // REQUIRED Constructor for loading from DB
    public ChequeAccount(int id, int customerId, String accountNumber, String branch, double balance) {
        super(id, customerId, accountNumber, branch, "ChequeAccount", balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > balance)
            throw new RuntimeException("Insufficient funds.");
        balance -= amount;
    }
}
