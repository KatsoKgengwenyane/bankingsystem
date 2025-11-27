package model;

public class SavingsAccount extends Account implements InterestBearing {

    private static final double INTEREST_RATE = 0.0005;

    // Constructor used when creating an account before DB insert
    public SavingsAccount(int customerId, String accountNumber, String branch) {
        super(customerId, accountNumber, branch, "SavingsAccount");
    }

    // Constructor with initial balance
    public SavingsAccount(int customerId, String accountNumber, String branch, double initialBalance) {
        super(customerId, accountNumber, branch, "SavingsAccount");
        if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative.");
        this.balance = initialBalance;
    }

    // REQUIRED constructor for loading from DB
    public SavingsAccount(int id, int customerId, String accountNumber, String branch, double balance) {
        super(id, customerId, accountNumber, branch, "SavingsAccount", balance);
    }

    @Override
    public void withdraw(double amount) {
        throw new RuntimeException("Withdrawals not allowed from Savings Accounts.");
    }

    @Override
    public void applyInterest() {
        balance += (balance * INTEREST_RATE);
    }
}
