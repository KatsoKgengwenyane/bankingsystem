package database;

import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public int insertCustomer(Customer c) {
        String sql = "INSERT INTO customers(first_name, surname, address) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getSurname());
            ps.setString(3, c.getAddress());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return -1;
    }

    public Customer findByFullName(String fullName) {
        String[] parts = fullName.split(" ");
        if (parts.length < 2) return null;

        String first = parts[0];
        String last = parts[1];

        String sql = "SELECT * FROM customers WHERE first_name = ? AND surname = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, first);
            ps.setString(2, last);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("address")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("address")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }
}
