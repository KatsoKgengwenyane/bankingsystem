import java.util.ArrayList;
import java.util.List;

// ------------------- INTERFACE -------------------
/**
 * Interface defining interest-bearing accounts.
 * Demonstrates abstraction and polymorphism.
 */
interface InterestBearing {
    void applyInterest();
}

// ------------------- CUSTOMER -------------------
/**
 * Represents a bank customer who can have multiple accounts.
 */
class Customer {
    private String firstName;
    private String surname;
    private String address;
    private List<Account> accounts;

    public Customer(String firstName, String surname, String address) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    @Override
    public String toString() {
        return "Customer: " + getFullName() + ", Address: " + address + ", Accounts: " + accounts.size();
    }
}

// ------------------- ABSTRACT ACCOUNT -------------------
/**
 * Abstract class representing a generic bank account.
 * Demonstrates abstraction and inheritance.
 */
abstract class Account {
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBranch() {
        return branch;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + accountNumber + "] | Balance: " + balance + " | Branch: " + branch;
    }
}

// ------------------- SAVINGS ACCOUNT -------------------
/**
 * Savings account earns small interest and does not allow withdrawals.
 */
class SavingsAccount extends Account implements InterestBearing {
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

// ------------------- INVESTMENT ACCOUNT -------------------
/**
 * Investment account allows deposits, withdrawals and earns 5% monthly interest.
 */
class InvestmentAccount extends Account implements InterestBearing {
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

// ------------------- CHEQUE ACCOUNT -------------------
/**
 * Cheque account used for salary deposits. Allows both deposits and withdrawals.
 */
class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String branch, Customer customer,
                         String employerName, String employerAddress) {
        super(accountNumber, branch, customer);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrew " + amount + " from ChequeAccount.");
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | Employer: " + employerName;
    }
}

// ------------------- BANK -------------------
/**
 * Represents a bank that manages customers and their accounts.
 */
class Bank {
    private List<Customer> customers;

    public Bank() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Customer findCustomer(String name) {
        for (Customer c : customers) {
            if (c.getFullName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println(c);
            for (Account acc : c.getAccounts()) {
                System.out.println("   " + acc);
            }
        }
    }
}

// ------------------- MAIN TEST -------------------
/**
 * Test class for Task 4 - Implementation of Core Model
 */
public class Main {
    public static void main(String[] args) {
        // Create a Bank instance
        Bank bank = new Bank();

        // Create a customer
        Customer customer1 = new Customer("John", "Doe", "Gaborone");
        bank.addCustomer(customer1);

        // Create and add accounts
        SavingsAccount savings = new SavingsAccount("S001", "Main Branch", customer1);
        InvestmentAccount investment = new InvestmentAccount("I001", "Main Branch", customer1, 1000);
        ChequeAccount cheque = new ChequeAccount("C001", "Main Branch", customer1, "Tech Corp", "Gaborone");

        customer1.addAccount(savings);
        customer1.addAccount(investment);
        customer1.addAccount(cheque);

        // Perform transactions
        savings.deposit(2000);
        investment.deposit(500);
        cheque.deposit(3000);

        // Apply interest
        savings.applyInterest();
        investment.applyInterest();

        // Withdraw from cheque account
        cheque.withdraw(1000);

        // Display all customers and accounts
        System.out.println("\n--- BANK CUSTOMER DETAILS ---");
        bank.displayAllCustomers();
    }
}
