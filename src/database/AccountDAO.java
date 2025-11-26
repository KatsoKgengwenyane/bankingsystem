package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // =====================================================
    // INSERT ACCOUNT
    // =====================================================
    public int insertAccount(Account account) {

        String sql = "INSERT INTO accounts(customer_id, account_number, branch, type, balance) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, account.getCustomerId());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getBranch());
            ps.setString(4, account.getType());  // MUST MATCH “SavingsAccount”
            ps.setDouble(5, account.getBalance());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =====================================================
    // GET ALL ACCOUNTS FOR CUSTOMER
    // =====================================================
    public List<Account> findByCustomer(int customerId) {
        List<Account> list = new ArrayList<>();

        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String accNum = rs.getString("account_number");
                String branch = rs.getString("branch");
                String type = rs.getString("type");
                double balance = rs.getDouble("balance");

                Account acc = createAccount(id, customerId, accNum, branch, type, balance);
                list.add(acc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // RECONSTRUCT ACCOUNT OBJECT FROM DB
    // =====================================================
    private Account createAccount(int id,
                                  int customerId,
                                  String number,
                                  String branch,
                                  String type,
                                  double balance) {

        Account acc;

        switch (type) {

            case "SavingsAccount":
                acc = new SavingsAccount(id, customerId, number, branch, balance);
                break;

            case "InvestmentAccount":
                acc = new InvestmentAccount(id, customerId, number, branch, balance);
                break;

            case "ChequeAccount":
                acc = new ChequeAccount(id, customerId, number, branch, balance);
                break;

            default:
                // fallback
                acc = new SavingsAccount(id, customerId, number, branch, balance);
        }

        return acc;
    }

    // =====================================================
    // UPDATE BALANCE
    // =====================================================
    public boolean updateBalance(String accountNumber, double newBalance) {

        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
