package model;

public abstract class Account {

    protected int id;            // database primary key
    protected int customerId;    // FK to customer
    protected String accountNumber;
    protected String branch;
    protected String type;
    protected double balance;

    public Account(int id, int customerId, String accountNumber, String branch, String type, double balance) {
        this.id = id;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.type = type;
        this.balance = balance;
    }

    public Account(int customerId, String accountNumber, String branch, String type) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.type = type;
        this.balance = 0.0;
    }

    // COMMON LOGIC
    public void deposit(double amount) {
        balance += amount;
    }

    public abstract void withdraw(double amount);

    // GETTERS
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public String getType() { return type; }
    public double getBalance() { return balance; }

    // SETTER
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
