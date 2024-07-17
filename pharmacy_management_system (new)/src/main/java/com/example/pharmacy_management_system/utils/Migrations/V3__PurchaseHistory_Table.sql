CREATE TABLE IF NOT EXISTS purchase_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    drug_name TEXT NOT NULL,
    purchase_date TEXT NOT NULL UNIQUE,
    buyer TEXT NOT NULL,
    quantity TEXT NOT NULL,
    total_amount REAL NOT NULL
);