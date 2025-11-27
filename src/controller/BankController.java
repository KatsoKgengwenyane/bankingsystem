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

    // ======================================================================================
    // CREATE CUSTOMER
    // ======================================================================================
    public String createCustomer(Customer c) {
        if (c == null) return "❌ Invalid customer.";
        if (c.getFirstName() == null || c.getFirstName().isBlank() ||
            c.getSurname() == null || c.getSurname().isBlank()) {
            return "❌ Customer must have a first name and surname.";
        }

        try {
            customerDAO.insert(c);
            return "✅ Customer created successfully.";
        } catch (Exception e) {
            return "❌ Error creating customer: " + e.getMessage();
        }
    }

    // ======================================================================================
    // SEARCH / LOAD CUSTOMER (by full name "First Last")
    // ======================================================================================
    public Customer searchCustomer(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return null;
        return loadCustomer(fullName.trim());
    }

    // ======================================================================================
    // UPDATE CUSTOMER
    // ======================================================================================
    public String updateCustomer(Customer c) {
        if (c == null || c.getId() <= 0) return "❌ Invalid customer to update.";
        try {
            customerDAO.update(c);
            return "✅ Customer information updated.";
        } catch (Exception e) {
            return "❌ Error updating customer: " + e.getMessage();
        }
    }

    // ======================================================================================
    // DELETE CUSTOMER
    // ======================================================================================
    public String deleteCustomer(int customerId) {
        if (customerId <= 0) return "❌ Invalid customer id.";
        try {
            customerDAO.delete(customerId);
            return "🗑 Customer deleted successfully.";
        } catch (Exception e) {
            return "❌ Error deleting customer: " + e.getMessage();
        }
    }

    // ======================================================================================
    // CREATE ACCOUNT (customer must already exist)
    // ======================================================================================
    public String createAccount(Customer c, String accountType, String accountNumber, String branch, double initial) {
        if (c == null) return "❌ Customer not found.";
        if (accountType == null || accountNumber == null || accountNumber.isBlank())
            return "❌ Provide account type and account number.";

        Account acc;
        try {
            switch (accountType) {
                case "SavingsAccount" ->
                    acc = new SavingsAccount(c.getId(), accountNumber.trim(), branch == null ? "" : branch.trim(), initial);

                case "InvestmentAccount" ->
                    acc = new InvestmentAccount(c.getId(), accountNumber.trim(), branch == null ? "" : branch.trim(), initial);

                case "ChequeAccount" ->
                    acc = new ChequeAccount(c.getId(), accountNumber.trim(), branch == null ? "" : branch.trim(), initial);

                default -> {
                    return "❌ Invalid account type.";
                }
            }
        } catch (Exception e) {
            return "❌ " + e.getMessage();
        }

        try {
            accountDAO.insert(acc);
            return "✅ Account created successfully.";
        } catch (Exception e) {
            return "❌ Error creating account: " + e.getMessage();
        }
    }

    // ======================================================================================
    // LOAD CUSTOMER + FRESH ACCOUNTS FROM DB
    // ======================================================================================
    public Customer loadCustomer(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return null;

        Customer c = customerDAO.findByFullName(fullName.trim());
        if (c == null) return null;

        List<Account> accounts = accountDAO.findByCustomer(c.getId());
        c.setAccounts(accounts); // replace list with fresh DB state
        return c;
    }

    // ======================================================================================
    // DEPOSIT (identify account by account number or by type)
    // ======================================================================================
    public String deposit(String fullName, String accountIdentifier, double amount) {
        if (fullName == null || fullName.isBlank()) return "⚠ Enter customer name.";
        if (accountIdentifier == null || accountIdentifier.isBlank()) return "⚠ Choose an account.";
        if (amount <= 0) return "⚠ Deposit must be greater than zero.";

        Customer c = loadCustomer(fullName.trim());
        if (c == null) return "❌ Customer not found.";

        Account acc = findAccountByNumberOrType(c, accountIdentifier);
        if (acc == null) return "❌ Account not found.";

        try {
            acc.deposit(amount);
        } catch (Exception e) {
            return "❌ " + e.getMessage();
        }

        accountDAO.updateBalance(acc.getAccountNumber(), acc.getBalance());
        transactionDAO.insert(acc.getId(), "DEPOSIT", amount);

        return "✅ Deposit successful. New balance: " + acc.getBalance();
    }

    // ======================================================================================
    // WITHDRAW (identify account by account number or by type)
    // ======================================================================================
    public String withdraw(String fullName, String accountIdentifier, double amount) {
        if (fullName == null || fullName.isBlank()) return "⚠ Enter customer name.";
        if (accountIdentifier == null || accountIdentifier.isBlank()) return "⚠ Choose an account.";
        if (amount <= 0) return "⚠ Withdrawal amount must be greater than zero.";

        Customer c = loadCustomer(fullName.trim());
        if (c == null) return "❌ Customer not found.";

        Account acc = findAccountByNumberOrType(c, accountIdentifier);
        if (acc == null) return "❌ Account not found.";

        try {
            acc.withdraw(amount);
        } catch (Exception e) {
            return "❌ " + e.getMessage();
        }

        accountDAO.updateBalance(acc.getAccountNumber(), acc.getBalance());
        transactionDAO.insert(acc.getId(), "WITHDRAW", amount);

        return "💸 Withdrawal successful. New balance: " + acc.getBalance();
    }

    // ======================================================================================
    // GET BALANCE
    // ======================================================================================
    public Double getBalance(String fullName, String accountIdentifier) {
        Customer c = loadCustomer(fullName);
        if (c == null) return null;

        Account acc = findAccountByNumberOrType(c, accountIdentifier);
        if (acc == null) return null;

        return acc.getBalance();
    }

    // ======================================================================================
    // LIST TRANSACTIONS FOR AN ACCOUNT (by Account object)
    // ======================================================================================
    public List<Transaction> getAccountTransactions(Account acc) {
        if (acc == null) return null;
        return transactionDAO.getTransactionsForAccount(acc.getId());
    }

    // ======================================================================================
    // HELPER: find account by account number OR by account type (case-insensitive)
    // Accepts either the account number or the textual type (e.g., "SavingsAccount")
    // ======================================================================================
    private Account findAccountByNumberOrType(Customer c, String identifier) {
        if (c == null || identifier == null) return null;

        String idTrim = identifier.trim();
        // 1) match by accountNumber exact
        for (Account a : c.getAccounts()) {
            if (a.getAccountNumber() != null && a.getAccountNumber().equalsIgnoreCase(idTrim)) {
                return a;
            }
        }

        // 2) match by type (normalized)
        String norm = idTrim.replace(" ", "").toLowerCase();
        for (Account a : c.getAccounts()) {
            if (a.getType() != null && a.getType().replace(" ", "").toLowerCase().equals(norm)) {
                return a;
            }
        }

        return null;
    }
}
