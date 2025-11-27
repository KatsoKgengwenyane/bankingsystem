package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // ------------------------------------------------------------
    // INSERT NEW ACCOUNT
    // ------------------------------------------------------------
    public void insert(Account account) {

        String sql = """
            INSERT INTO accounts (customer_id, account_number, branch, type, balance)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, account.getCustomerId());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getBranch());
            ps.setString(4, account.getType());
            ps.setDouble(5, account.getBalance());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------
    // GET ALL ACCOUNTS FOR A CUSTOMER
    // ------------------------------------------------------------
    public List<Account> findByCustomer(int customerId) {

        List<Account> accounts = new ArrayList<>();

        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                accounts.add(createAccountFromDB(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // ------------------------------------------------------------
    // CREATE PROPER ACCOUNT OBJECT FROM DATABASE ROW
    // ------------------------------------------------------------
    private Account createAccountFromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        int customerId = rs.getInt("customer_id");
        String accNum = rs.getString("account_number");
        String branch = rs.getString("branch");
        String type = rs.getString("type");
        double balance = rs.getDouble("balance");

        // Match database type to real object
        return switch (type) {
            case "SavingsAccount" -> new SavingsAccount(
                    id, customerId, accNum, branch, balance
            );

            case "InvestmentAccount" -> new InvestmentAccount(
                    id, customerId, accNum, branch, balance
            );

            case "ChequeAccount" -> new ChequeAccount(
                    id, customerId, accNum, branch, balance
            );

            default -> {
                System.out.println("⚠ Unknown account type: " + type);
                yield new SavingsAccount(id, customerId, accNum, branch, balance);
            }
        };
    }

    // ------------------------------------------------------------
    // UPDATE BALANCE AFTER DEPOSIT/WITHDRAWAL
    // ------------------------------------------------------------
    public void updateBalance(String accountNumber, double newBalance) {

        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
