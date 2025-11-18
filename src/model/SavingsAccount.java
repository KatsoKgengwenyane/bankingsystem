package model;

public class SavingsAccount extends Account implements InterestBearing {

    private static final double INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
    }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Withdrawals are not allowed from Savings Accounts.");
    }

    @Override
    public void applyInterest() {
        double newBalance = getBalance() * (1 + INTEREST_RATE);
        setBalance(newBalance);
    }
}
