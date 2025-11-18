package model;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer c) { customers.add(c); }

    public List<Customer> getCustomers() { return customers; }

    public Customer findCustomerByName(String fullName) {
        for (Customer c : customers) {
            if (c.getFullName().equalsIgnoreCase(fullName)) {
                return c;
            }
        }
        return null;
    }

    public Account findAccountByType(Customer c, String typeName) {
        for (Account a : c.getAccounts()) {
            if (a.getType().equalsIgnoreCase(typeName.replace(" ", ""))) {
                return a;
            }
        }
        return null;
    }
}
