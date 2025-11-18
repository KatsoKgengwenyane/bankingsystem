import model.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        Customer customer1 = new Customer("John", "Doe", "Gaborone");
        bank.addCustomer(customer1);

        SavingsAccount savings = new SavingsAccount("S001", "Main Branch", customer1);
        InvestmentAccount investment = new InvestmentAccount("I001", "Main Branch", customer1, 1000);
        ChequeAccount cheque = new ChequeAccount("C001", "Main Branch", customer1, "Tech Corp", "Gaborone");

        customer1.addAccount(savings);
        customer1.addAccount(investment);
        customer1.addAccount(cheque);

        savings.deposit(2000);
        investment.deposit(500);
        cheque.deposit(3000);

        savings.applyInterest();
        investment.applyInterest();

        cheque.withdraw(1000);

        System.out.println("\n--- BANK CUSTOMER DETAILS ---");
        bank.displayAllCustomers();
    }
}
