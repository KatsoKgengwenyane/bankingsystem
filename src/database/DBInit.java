package database;

import java.sql.Connection;
import java.sql.Statement;

public class DBInit {

    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create CUSTOMERS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    surname TEXT NOT NULL,
                    address TEXT,
                    cellphone TEXT,
                    employer TEXT,
                    company_name TEXT,
                    company_address TEXT,
                    is_company INTEGER DEFAULT 0
                );
            """);

            stmt.execute("""
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT UNIQUE NOT NULL,
        password_hash TEXT NOT NULL
    )
""");

            // Create ACCOUNTS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS accounts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    account_number TEXT NOT NULL,
                    branch TEXT,
                    type TEXT NOT NULL,
                    balance REAL DEFAULT 0,
                    FOREIGN KEY (customer_id) REFERENCES customers(id)
                );
            """);

            // Create TRANSACTIONS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_id INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (account_id) REFERENCES accounts(id)
                );
            """);

            System.out.println("✔ Database initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error initializing database: " + e.getMessage());
        }
    }
}
