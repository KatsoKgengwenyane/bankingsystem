package model;

public class InvestmentAccount extends Account implements InterestBearing {

    private static final double INTEREST_RATE = 0.05;

    public InvestmentAccount(int customerId, String accountNumber, String branch, double initialDeposit) {
        super(customerId, accountNumber, branch, "InvestmentAccount");
        if (initialDeposit < 500) {
            throw new RuntimeException("Initial deposit must be at least 500.");
        }
        this.balance = initialDeposit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > balance) {
            throw new RuntimeException("Insufficient funds.");
        }
        balance -= amount;
    }

    @Override
    public void applyInterest() {
        balance += (balance * INTEREST_RATE);
    }
}
