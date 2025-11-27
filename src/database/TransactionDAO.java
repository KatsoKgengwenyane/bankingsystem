package database;

import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // ------------------------------------------------------------
    // INSERT NEW TRANSACTION
    // ------------------------------------------------------------
    public void insert(int accountId, String type, double amount) {
        String sql = """
            INSERT INTO transactions (account_id, type, amount)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------
    // GET ALL TRANSACTIONS FOR AN ACCOUNT
    // ------------------------------------------------------------
    public List<Transaction> getTransactionsForAccount(int accountId) {

        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractTransaction(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ------------------------------------------------------------
    // INTERNAL HELPER: MAP RESULTSET → Transaction OBJECT
    // ------------------------------------------------------------
    private Transaction extractTransaction(ResultSet rs) throws SQLException {
        // match constructor: Transaction(int id, int accountId, String type, double amount, String timestamp)
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("account_id"),
                rs.getString("type"),
                rs.getDouble("amount"),
                rs.getString("timestamp")
        );
    }
}
