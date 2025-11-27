package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private int id;
    private String firstName;
    private String surname;
    private String address;
    private String cellphone;
    private String employer;
    private String companyName;
    private String companyAddress;
    private boolean isCompany;

    private List<Account> accounts = new ArrayList<>();

    // ===================== CONSTRUCTORS =====================

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

    // ===================== GETTERS =====================

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getFullName() { return firstName + " " + surname; }
    public String getAddress() { return address; }
    public String getCellphone() { return cellphone; }
    public String getEmployer() { return employer; }
    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
    public boolean isCompany() { return isCompany; }
    public List<Account> getAccounts() { return accounts; }

    // ===================== SETTERS =====================

    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAddress(String address) { this.address = address; }
    public void setCellphone(String cellphone) { this.cellphone = cellphone; }
    public void setEmployer(String employer) { this.employer = employer; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }
    public void setCompany(boolean company) { isCompany = company; }

    // ===================== ACCOUNT MANAGEMENT =====================

    /** Add account only if not already present */
    public void addAccount(Account acc) {
        if (acc == null) return;
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(acc.getAccountNumber())) {
                return; // avoid duplicates
            }
        }
        accounts.add(acc);
    }

    /** Remove account by its number */
    public boolean removeAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) return false;
        return accounts.removeIf(a -> a.getAccountNumber().equals(accountNumber));
    }

    /** Replace the entire list (used when refreshing from DB) */
    public void setAccounts(List<Account> newList) {
        accounts.clear();
        if (newList != null) accounts.addAll(newList);
    }

    // ===================== UTILITY =====================

    @Override
    public String toString() {
        return "Customer{id=" + id +
                ", name='" + getFullName() + '\'' +
                ", address='" + address + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", employer='" + employer + '\'' +
                ", isCompany=" + isCompany +
                ", accounts=" + accounts.size() +
                '}';
    }
}
