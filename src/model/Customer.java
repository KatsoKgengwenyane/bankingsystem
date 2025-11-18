package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String firstName;
    private String surname;
    private String address;

    private List<Account> accounts = new ArrayList<>();

    public Customer(int id, String firstName, String surname, String address) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
    }

    public Customer(String firstName, String surname, String address) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    public String getAddress() {
        return address;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    // ADD ACCOUNT
    public void addAccount(Account acc) {
        accounts.add(acc);
    }
}
