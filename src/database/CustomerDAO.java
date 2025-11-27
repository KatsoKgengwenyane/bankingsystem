package database;

import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // ------------------------------------------------------------
    // INSERT CUSTOMER
    // ------------------------------------------------------------
    public int insert(Customer c) throws Exception {

        String sql = """
            INSERT INTO customers
            (first_name, surname, address, cellphone, employer,
             company_name, company_address, is_company)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getSurname());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getCellphone());
            ps.setString(5, c.getEmployer());
            ps.setString(6, c.getCompanyName());
            ps.setString(7, c.getCompanyAddress());
            ps.setInt(8, c.isCompany() ? 1 : 0);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new Exception("Insert failed — no rows affected.");

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

            throw new Exception("Customer created but no ID returned.");

        } catch (SQLException e) {
            throw new Exception("DB Insert Error: " + e.getMessage());
        }
    }


    // ------------------------------------------------------------
    // FIND CUSTOMER BY FULL NAME
    // ------------------------------------------------------------
    public Customer findByFullName(String fullName) {

        if (fullName == null || !fullName.contains(" "))
            return null;

        String[] parts = fullName.trim().split(" ", 2);
        String first = parts[0].trim();
        String last = parts[1].trim();

        String sql = """
            SELECT *
            FROM customers
            WHERE LOWER(first_name) = LOWER(?)
              AND LOWER(surname)    = LOWER(?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, first);
            ps.setString(2, last);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCustomer(rs);
            }

        } catch (SQLException e) {
            System.out.println("ERROR in findByFullName: " + e.getMessage());
        }

        return null;
    }


    // ------------------------------------------------------------
    // GET ALL CUSTOMERS
    // ------------------------------------------------------------
    public List<Customer> getAll() {

        List<Customer> list = new ArrayList<>();

        String sql = "SELECT * FROM customers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractCustomer(rs));
            }

        } catch (SQLException e) {
            System.out.println("ERROR loading customers: " + e.getMessage());
        }

        return list;
    }

    

    // ------------------------------------------------------------
    // UPDATE CUSTOMER
    // ------------------------------------------------------------
    public void update(Customer c) {

        String sql = """
            UPDATE customers
            SET first_name=?, surname=?, address=?, cellphone=?, employer=?,
                company_name=?, company_address=?, is_company=?
            WHERE id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getSurname());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getCellphone());
            ps.setString(5, c.getEmployer());
            ps.setString(6, c.getCompanyName());
            ps.setString(7, c.getCompanyAddress());
            ps.setInt(8, c.isCompany() ? 1 : 0);
            ps.setInt(9, c.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("DB Update Error: " + e.getMessage());
        }
    }


    // ------------------------------------------------------------
    // DELETE CUSTOMER
    // ------------------------------------------------------------
    public void delete(int id) {

        String sql = "DELETE FROM customers WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("DB Delete Error: " + e.getMessage());
        }
    }


    // ------------------------------------------------------------
    // Convert ResultSet → Customer object
    // ------------------------------------------------------------
    private Customer extractCustomer(ResultSet rs) throws SQLException {

        Customer c = new Customer(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getString("address")
        );

        c.setCellphone(rs.getString("cellphone"));
        c.setEmployer(rs.getString("employer"));
        c.setCompanyName(rs.getString("company_name"));
        c.setCompanyAddress(rs.getString("company_address"));
        c.setCompany(rs.getInt("is_company") == 1);

        return c;
    }
}
