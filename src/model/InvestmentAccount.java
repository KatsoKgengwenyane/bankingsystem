package model;

public class InvestmentAccount extends Account implements InterestBearing {

    private static final double MIN_DEPOSIT = 500;
    private static final double INTEREST_RATE = 0.01;

    // Constructor for NEW accounts
    public InvestmentAccount(int customerId, String accountNumber, String branch, double initialDeposit) {
        super(customerId, accountNumber, branch, "InvestmentAccount");

        if (initialDeposit < MIN_DEPOSIT) {
            throw new RuntimeException("Initial deposit for InvestmentAccount must be at least 500.");
        }

        this.balance = initialDeposit;
    }

    // Constructor for LOADING EXISTING accounts from DB
    public InvestmentAccount(int id, int customerId, String accountNumber, String branch, double balance) {
        super(id, customerId, accountNumber, branch, "InvestmentAccount", balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > balance)
            throw new RuntimeException("Insufficient funds.");
        balance -= amount;
    }

    @Override
    public void applyInterest() {
        balance += balance * INTEREST_RATE;
    }
}
