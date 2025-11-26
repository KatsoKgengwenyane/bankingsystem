package model;

public class ChequeAccount extends Account {

    // For creating NEW accounts
    public ChequeAccount(int customerId, String accountNumber, String branch) {
        super(customerId, accountNumber, branch, "ChequeAccount");
    }

    // For LOADING accounts from database
    public ChequeAccount(int id, int customerId, String accountNumber, String branch, double balance) {
        super(id, customerId, accountNumber, branch, "ChequeAccount", balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > balance) {
            throw new RuntimeException("Insufficient funds.");
        }
        balance -= amount;
    }
}
