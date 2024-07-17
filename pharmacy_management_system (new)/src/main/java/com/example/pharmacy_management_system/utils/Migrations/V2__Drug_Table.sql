CREATE TABLE IF NOT EXISTS drugs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    drug_name TEXT NOT NULL,
    description TEXT NOT NULL UNIQUE,
    quantity TEXT NOT NULL,
    price TEXT NOT NULL,
    supplier_id INTEGER,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);