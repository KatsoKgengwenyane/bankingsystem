package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private int id;
    private int accountId;
    private String type; // DEPOSIT, WITHDRAW
    private double amount;
    private String timestamp; // stored as "yyyy-MM-dd HH:mm:ss"

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructor used when creating a new transaction
    public Transaction(int accountId, String type, double amount) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now().format(FMT);
    }

    // Constructor used when loading from DB (REQUIRED!)
    public Transaction(int id, int accountId, String type, double amount, String timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + amount;
    }
}
