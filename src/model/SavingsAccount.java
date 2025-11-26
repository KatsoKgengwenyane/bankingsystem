package model;

public class SavingsAccount extends Account implements InterestBearing {

    private static final double INTEREST_RATE = 0.0005;

    public SavingsAccount(int customerId, String accountNumber, String branch) {
        super(customerId, accountNumber, branch, "SavingsAccount");
    }
 
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