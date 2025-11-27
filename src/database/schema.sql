DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS transactions;

CREATE TABLE customers (
    id INTEGER PRIMARY KEY,
    first_name TEXT NOT NULL,
    surname TEXT NOT NULL,
    address TEXT,
    cellphone TEXT,
    employer TEXT,
    company_name TEXT,
    company_address TEXT,
    is_company INTEGER DEFAULT 0
);

CREATE TABLE accounts (
    id INTEGER PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    account_number TEXT UNIQUE NOT NULL,
    branch TEXT,
    type TEXT NOT NULL,
    balance REAL DEFAULT 0,
    FOREIGN KEY(customer_id) REFERENCES customers(id)
);

CREATE TABLE transactions (
    id INTEGER PRIMARY KEY,
    account_id INTEGER NOT NULL,
    type TEXT NOT NULL,
    amount REAL NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(account_id) REFERENCES accounts(id)
);
