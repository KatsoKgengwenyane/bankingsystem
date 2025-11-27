package model;

public class InvestmentAccount extends Account implements InterestBearing {

    private static final double MIN = 500;
    private static final double RATE = 0.01;

    // For account creation
    public InvestmentAccount(int customerId, String accountNumber, String branch, double initial) {
        super(customerId, accountNumber, branch, "InvestmentAccount");
        if (initial < MIN)
            throw new RuntimeException("Minimum deposit for Investment account is 500.");
        this.balance = initial;
    }

    // For loading from DB
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
        balance += balance * RATE;
    }
}
