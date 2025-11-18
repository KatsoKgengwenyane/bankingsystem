package model;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals not allowed from SavingsAccount.");
    }

    @Override
    public void applyInterest() {
        double newBalance = getBalance() + (getBalance() * INTEREST_RATE);
        setBalance(newBalance);
        System.out.println("Interest applied to SavingsAccount [" + getAccountNumber() + "]: " + getBalance());
    }
}
