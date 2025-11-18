package model;

public abstract class Account {
    private String accountNumber;
    private double balance;
    private String branch;
    private Customer customer;

    public Account(String accountNumber, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.customer = customer;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited " + amount + " into " + accountNumber);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public abstract void withdraw(double amount);

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + accountNumber + "] | Balance: " + balance + " | Branch: " + branch;
    }
}
