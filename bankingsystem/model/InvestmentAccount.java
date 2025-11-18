package model;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05; // 5%
    private static final double MIN_DEPOSIT = 500.0;

    public InvestmentAccount(String accountNumber, String branch, Customer customer, double initialDeposit) {
        super(accountNumber, branch, customer);
        if (initialDeposit >= MIN_DEPOSIT) {
            setBalance(initialDeposit);
        } else {
            throw new IllegalArgumentException("Initial deposit must be at least " + MIN_DEPOSIT);
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrew " + amount + " from InvestmentAccount.");
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    @Override
    public void applyInterest() {
        double newBalance = getBalance() + (getBalance() * INTEREST_RATE);
        setBalance(newBalance);
        System.out.println("Interest applied to InvestmentAccount [" + getAccountNumber() + "]: " + getBalance());
    }
}
