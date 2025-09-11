import java.util.ArrayList;
import java.util.List;

// ------------------- INTERFACE -------------------
interface InterestBearing {
    void applyInterest();
}

// ------------------- CUSTOMER -------------------
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

    // Getters and Setters (optional for later)
}

// ------------------- ABSTRACT ACCOUNT -------------------
abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;

    public Account(String accountNumber, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.customer = customer;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public abstract void withdraw(double amount);
}

// ------------------- SAVINGS ACCOUNT -------------------
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
        balance += balance * INTEREST_RATE;
    }
}

// ------------------- INVESTMENT ACCOUNT -------------------
class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05; // 5%
    private static final double MIN_DEPOSIT = 500.0;

    public InvestmentAccount(String accountNumber, String branch, Customer customer, double initialDeposit) {
        super(accountNumber, branch, customer);
        if (initialDeposit >= MIN_DEPOSIT) {
            this.balance = initialDeposit;
        } else {
            throw new IllegalArgumentException("Initial deposit must be at least " + MIN_DEPOSIT);
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void applyInterest() {
        balance += balance * INTEREST_RATE;
    }
}

// ------------------- CHEQUE ACCOUNT -------------------
class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String branch, Customer customer, String employerName, String employerAddress) {
        super(accountNumber, branch, customer);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

// ------------------- MAIN TEST -------------------
public class Main {
    public static void main(String[] args) {
        // Create a customer
        Customer customer1 = new Customer("John", "Doe", "Gaborone");

        // Add accounts
        SavingsAccount savings = new SavingsAccount("S001", "Main Branch", customer1);
        InvestmentAccount investment = new InvestmentAccount("I001", "Main Branch", customer1, 1000);
        ChequeAccount cheque = new ChequeAccount("C001", "Main Branch", customer1, "Tech Corp", "Gaborone");

        customer1.addAccount(savings);
        customer1.addAccount(investment);
        customer1.addAccount(cheque);

        // Test operations
        savings.deposit(2000);
        investment.deposit(500);
        cheque.deposit(3000);

        savings.applyInterest();
        investment.applyInterest();

        cheque.withdraw(1000);

        System.out.println("Savings Balance: " + savings.balance);
        System.out.println("Investment Balance: " + investment.balance);
        System.out.println("Cheque Balance: " + cheque.balance);
    }
}
