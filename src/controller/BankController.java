package controller;

import database.AccountDAO;
import database.CustomerDAO;
import database.TransactionDAO;
import model.*;

import java.util.List;

public class BankController {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public BankController() {}

    // ============================================================
    // LOAD CUSTOMER + THEIR ACCOUNTS
    // ============================================================
    public Customer loadCustomer(String fullName) {

        if (fullName == null || fullName.trim().isEmpty())
            return null;

        Customer c = customerDAO.findByFullName(fullName.trim());
        if (c == null) return null;

        List<Account> accounts = accountDAO.findByCustomer(c.getId());
        for (Account a : accounts) {
            c.addAccount(a);
        }

        return c;
    }

    // ============================================================
    // DEPOSIT
    // ============================================================
    public String deposit(String fullName, String accountType, double amount) {

        // Validate input
        if (fullName == null || fullName.isBlank())
            return "⚠ Please enter customer name.";

        if (accountType == null)
            return "⚠ Please choose an account type.";

        if (amount <= 0)
            return "⚠ Deposit amount must be greater than zero.";

        // Load customer
        Customer c = loadCustomer(fullName.trim());
        if (c == null) return "❌ Customer not found.";

        // Find account
        Account acc = findAccount(c, accountType);
        if (acc == null) return "❌ Account not found for this customer.";

        // Apply deposit
        try {
            acc.deposit(amount);
        } catch (Exception e) {
            return "❌ " + e.getMessage();
        }

        // Persist changes
        accountDAO.updateBalance(acc.getAccountNumber(), acc.getBalance());
        transactionDAO.insert(acc.getId(), "DEPOSIT", amount);

        return "✅ Deposit successful. New balance: " + acc.getBalance();
    }

    // ============================================================
    // WITHDRAW
    // ============================================================
    public String withdraw(String fullName, String accountType, double amount) {

        // Validate input
        if (fullName == null || fullName.isBlank())
            return "⚠ Please enter customer name.";

        if (accountType == null)
            return "⚠ Please choose an account type.";

        if (amount <= 0)
            return "⚠ Withdrawal amount must be greater than zero.";

        // Load customer
        Customer c = loadCustomer(fullName.trim());
        if (c == null) return "❌ Customer not found.";

        // Find account
        Account acc = findAccount(c, accountType);
        if (acc == null) return "❌ Account not found for this customer.";

        // Attempt withdrawal
        try {
            acc.withdraw(amount);
        } catch (Exception e) {
            return "❌ " + e.getMessage();
        }

        // Persist changes
        accountDAO.updateBalance(acc.getAccountNumber(), acc.getBalance());
        transactionDAO.insert(acc.getId(), "WITHDRAW", amount);

        return "💸 Withdrawal successful. New balance: " + acc.getBalance();
    }

    // ============================================================
    // HELPER: FIND ACCOUNT BY TYPE (Robust and safe)
    // ============================================================
    private Account findAccount(Customer c, String accountType) {

        String normalized = accountType.replace(" ", "").trim().toLowerCase();

        for (Account a : c.getAccounts()) {
            if (a.getType().toLowerCase().equals(normalized)) {
                return a;
            }
        }
        return null;
    }
}
