package database;

import java.sql.*;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:bank.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
