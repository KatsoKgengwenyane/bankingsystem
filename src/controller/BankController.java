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

    // Helper to convert GUI names to class names
    private String mapAccountType(String guiType) {
        switch (guiType) {
            case "Savings Account":
                return "SavingsAccount";
            case "Investment Account":
                return "InvestmentAccount";
            case "Cheque Account":
                return "ChequeAccount";
            default:
                return null;
        }
    }

    public String handleDeposit(String customerName, String accountType, double amount) {
        if (customerName.isEmpty() || accountType == null) {
            return "⚠️ Please enter customer name and select an account type.";
        }

        Customer customer = bank.findCustomerByName(customerName);
        if (customer == null) {
            return "❌ Customer not found.";
        }

        String type = mapAccountType(accountType);
        Account account = bank.findAccountByType(customer, type);
        if (account == null) {
            return "❌ Customer does not have a " + accountType + ".";
        }

        account.deposit(amount);
        return "✅ Deposit successful: " + amount + "\nNew balance: " + account.getBalance();
    }

    public String handleWithdraw(String customerName, String accountType, double amount) {
        if (customerName.isEmpty() || accountType == null) {
            return "⚠️ Please enter customer name and select an account type.";
        }

        Customer customer = bank.findCustomerByName(customerName);
        if (customer == null) {
            return "❌ Customer not found.";
        }

        String type = mapAccountType(accountType);
        Account account = bank.findAccountByType(customer, type);
        if (account == null) {
            return "❌ Customer does not have a " + accountType + ".";
        }

        try {
            account.withdraw(amount);
        } catch (UnsupportedOperationException ex) {
            return "❌ " + ex.getMessage();
        }

        return "💸 Withdrawal successful: " + amount + "\nNew balance: " + account.getBalance();
    }
}

