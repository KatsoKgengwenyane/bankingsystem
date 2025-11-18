package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public int insertAccount(Account account) {
        String sql = "INSERT INTO accounts(customer_id, account_number, branch, type, balance) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, account.getCustomerId());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getBranch());
            ps.setString(4, account.getType());
            ps.setDouble(5, account.getBalance());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return -1;
    }

    public List<Account> findByCustomer(int customerId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                Account acc = createAccount(
                        rs.getInt("customer_id"),
                        rs.getString("account_number"),
                        rs.getString("branch"),
                        type,
                        rs.getDouble("balance")
                );
                list.add(acc);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    private Account createAccount(int customerId, String num, String branch, String type, double balance) {
        Account acc;

        switch (type) {
            case "SavingsAccount":
                acc = new SavingsAccount(customerId, num, branch);
                break;
            case "InvestmentAccount":
                acc = new InvestmentAccount(customerId, num, branch, 500);
                break;
            default:
                acc = new ChequeAccount(customerId, num, branch);
        }

        acc.setBalance(balance);
        return acc;
    }

    public boolean updateBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);

            return ps.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }
}
