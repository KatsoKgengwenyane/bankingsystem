package model;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> customers;

    public Bank() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println(c);
            for (Account acc : c.getAccounts()) {
                System.out.println("   " + acc);
            }
        }
    }


public Customer findCustomerByName(String fullName) {
    for (Customer c : customers) {
        String name = c.getFirstName() + " " + c.getSurname();
        if (name.equalsIgnoreCase(fullName.trim())) {
            return c;
        }
    }
    return null;
}

public Account findAccountByType(Customer customer, String accountType) {
    for (Account a : customer.getAccounts()) {
        if (a.getClass().getSimpleName().equalsIgnoreCase(accountType.replace(" ", ""))) {
            return a;
        }
    }
    return null;
}
}