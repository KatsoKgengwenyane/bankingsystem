package database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionDAO {

    public void insert(int accountId, String type, double amount) {
        String sql = "INSERT INTO transactions(account_id, type, amount) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
