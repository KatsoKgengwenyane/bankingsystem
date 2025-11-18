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
    // LOAD CUSTOMER + ACCOUNTS
    // ============================================================
    public Customer loadCustomer(String fullName) {
        Customer c = customerDAO.findByFullName(fullName);
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
        Customer c = loadCustomer(fullName);
        if (c == null) return "❌ Customer not found.";

        Account acc = findAccount(c, accountType);
        if (acc == null) return "❌ Account not found.";

        acc.deposit(amount);
        accountDAO.updateBalance(acc.getAccountNumber(), acc.getBalance());
        transactionDAO.insert(acc.getId(), "DEPOSIT", amount);

        return "✅ Deposit successful. New balance: " + acc.getBalance();
    }


    // ============================================================
    // WITHDRAW
    // ============================================================
    public String withdraw(String fullName, String accountType, double amount) {
        Customer c = loadCustomer(fullName);
        if (c == null) return "❌ Customer not found.";

        Account acc = findAccount(c, accountType);
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


    // ============================================================
    // HELPER: FIND ACCOUNT BY TYPE
    // ============================================================
    private Account findAccount(Customer c, String accountType) {
        String normalized = accountType.replace(" ", "").toLowerCase();

        for (Account a : c.getAccounts()) {
            if (a.getType().toLowerCase().equals(normalized)) {
                return a;
            }
        }
        return null;
    }
}
