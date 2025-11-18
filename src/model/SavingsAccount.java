package model;

public class SavingsAccount extends Account implements InterestBearing {

    private static final double INTEREST_RATE = 0.0005;

    public SavingsAccount(int customerId, String accountNumber, String branch) {
        super(customerId, accountNumber, branch, "SavingsAccount");
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
