package model;

public class ChequeAccount extends Account {

    public ChequeAccount(int customerId, String accountNumber, String branch) {
        super(customerId, accountNumber, branch, "ChequeAccount");
    }
    
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
