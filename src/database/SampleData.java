package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SampleData {

    // ------------------------------------------------------------
    // LOAD SAMPLE DATA (only if database is empty)
    // ------------------------------------------------------------
    public static void load() {
        try (Connection conn = DBConnection.getConnection()) {

            // --------------------------------------------------------
            // CHECK if customers already exist → Prevent duplicate rows
            // --------------------------------------------------------
            PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) AS total FROM customers");
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt("total") > 0) {
                System.out.println("Sample data already exists. Skipping insert.");
                return;
            }

            System.out.println("Loading sample data...");

            // --------------------------------------------------------
            // INSERT SAMPLE CUSTOMERS
            // --------------------------------------------------------
            PreparedStatement insertCustomer = conn.prepareStatement("""
                INSERT INTO customers 
                (first_name, surname, address, cellphone, employer, company_name, company_address, is_company)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """);

            // Customer 1 - Individual
            insertCustomer.setString(1, "John");
            insertCustomer.setString(2, "Doe");
            insertCustomer.setString(3, "Gaborone");
            insertCustomer.setString(4, "71234567");
            insertCustomer.setString(5, "Botswana Post");
            insertCustomer.setString(6, null);
            insertCustomer.setString(7, null);
            insertCustomer.setInt(8, 0);
            insertCustomer.executeUpdate();

            // Customer 2 - Individual
            insertCustomer.setString(1, "Jane");
            insertCustomer.setString(2, "Smith");
            insertCustomer.setString(3, "Francistown");
            insertCustomer.setString(4, "76543210");
            insertCustomer.setString(5, "BPC");
            insertCustomer.setString(6, null);
            insertCustomer.setString(7, null);
            insertCustomer.setInt(8, 0);
            insertCustomer.executeUpdate();

            // Customer 3 - Company
            insertCustomer.setString(1, "Mega");
            insertCustomer.setString(2, "Tech");
            insertCustomer.setString(3, "Gaborone CBD");
            insertCustomer.setString(4, "3930001");
            insertCustomer.setString(5, null);
            insertCustomer.setString(6, "MegaTech (Pty) Ltd");
            insertCustomer.setString(7, "Gaborone CBD Tower");
            insertCustomer.setInt(8, 1);
            insertCustomer.executeUpdate();

            // --------------------------------------------------------
            // INSERT SAMPLE ACCOUNTS
            // --------------------------------------------------------
            PreparedStatement insertAcc = conn.prepareStatement("""
                INSERT INTO accounts (customer_id, account_number, branch, type, balance)
                VALUES (?, ?, ?, ?, ?)
            """);

            // John Doe - Savings
            insertAcc.setInt(1, 1);
            insertAcc.setString(2, "ACC1001");
            insertAcc.setString(3, "Main Branch");
            insertAcc.setString(4, "SavingsAccount");
            insertAcc.setDouble(5, 5000);
            insertAcc.executeUpdate();

            // John Doe - Cheque
            insertAcc.setInt(1, 1);
            insertAcc.setString(2, "ACC1002");
            insertAcc.setString(3, "Main Branch");
            insertAcc.setString(4, "ChequeAccount");
            insertAcc.setDouble(5, 2500);
            insertAcc.executeUpdate();

            // Jane Smith - Investment
            insertAcc.setInt(1, 2);
            insertAcc.setString(2, "ACC2001");
            insertAcc.setString(3, "Main Branch");
            insertAcc.setString(4, "InvestmentAccount");
            insertAcc.setDouble(5, 800);
            insertAcc.executeUpdate();

            // Company - MegaTech - Cheque
            insertAcc.setInt(1, 3);
            insertAcc.setString(2, "ACC3001");
            insertAcc.setString(3, "Corporate Branch");
            insertAcc.setString(4, "ChequeAccount");
            insertAcc.setDouble(5, 15000);
            insertAcc.executeUpdate();

            System.out.println("Sample data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
