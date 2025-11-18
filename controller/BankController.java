package controller;

import model.*;
import view.MainView;

public class BankController {

    private Bank bank;
    private MainView view;

    public BankController(Bank bank, MainView view) {
        this.bank = bank;
        this.view = view;
    }

    // Called when user clicks Deposit
    public String handleDeposit(String customerName, String accountType, double amount) {
        if (customerName.isEmpty() || accountType == null) {
            return "⚠️ Please enter customer name and select an account type.";
        }

        Customer customer = bank.findCustomerByName(customerName);
        if (customer == null) {
            return "❌ Customer not found.";
        }

        Account account = bank.findAccountByType(customer, accountType);
        if (account == null) {
            return "❌ No " + accountType + " found for this customer.";
        }

        account.deposit(amount);
        return "✅ Deposited " + amount + " into " + accountType + ".\nNew balance: " + account.getBalance();
    }

    // Called when user clicks Withdraw
    public String handleWithdraw(String customerName, String accountType, double amount) {
        if (customerName.isEmpty() || accountType == null) {
            return "⚠️ Please enter customer name and select an account type.";
        }

        Customer customer = bank.findCustomerByName(customerName);
        if (customer == null) {
            return "❌ Customer not found.";
        }

        Account account = bank.findAccountByType(customer, accountType);
        if (account == null) {
            return "❌ No " + accountType + " found for this customer.";
        }

        if (amount > account.getBalance()) {
            return "❌ Insufficient funds. Current balance: " + account.getBalance();
        }

        account.withdraw(amount);
        return "💸 Withdrawn " + amount + " from " + accountType + ".\nNew balance: " + account.getBalance();
    }
}
