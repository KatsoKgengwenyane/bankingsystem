package database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SampleData {

    public static void load() {
        try (Connection conn = DBConnection.getConnection()) {

            // Insert sample customers
            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO customers(first_name, surname, address) VALUES (?, ?, ?)");
            ps1.setString(1, "John");
            ps1.setString(2, "Doe");
            ps1.setString(3, "Gaborone");
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO customers(first_name, surname, address) VALUES (?, ?, ?)");
            ps2.setString(1, "Jane");
            ps2.setString(2, "Smith");
            ps2.setString(3, "Francistown");
            ps2.executeUpdate();

            // Insert sample accounts
            PreparedStatement ps3 = conn.prepareStatement(
                    "INSERT INTO accounts(customer_id, account_number, branch, type, balance) VALUES (1, 'ACC1001', 'Main Branch', 'SavingsAccount', 5000)");
            ps3.executeUpdate();

            PreparedStatement ps4 = conn.prepareStatement(
                    "INSERT INTO accounts(customer_id, account_number, branch, type, balance) VALUES (2, 'ACC2001', 'Main Branch', 'ChequeAccount', 2000)");
            ps4.executeUpdate();

            System.out.println("Sample data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
