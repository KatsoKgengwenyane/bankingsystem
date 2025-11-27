package database;

import java.sql.*;
import java.security.MessageDigest;

public class UserDAO {

    // Convert password → SHA-256 hash
    private String hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // Register user
    public void createUser(String username, String password) throws Exception {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hash(password));
            ps.executeUpdate();
        }
    }

    // Validate login
    public boolean authenticate(String username, String password) throws Exception {
        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return false; // user not found

            String stored = rs.getString("password_hash");
            String check = hash(password);
            return stored.equals(check);
        }
    }

    // Forgot password: update hash
    public void updatePassword(String username, String newPassword) throws Exception {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hash(newPassword));
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }
}
