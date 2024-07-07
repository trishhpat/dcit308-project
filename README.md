# DCIT308 - Pharmacy Management System
![Pharmacy Management System Dashboard](https://github.com/trishhpat/dcit308-project/blob/master/Screenshot%20from%202024-06-23%2004-01-12.png)

## Project Overview

The Pharmacy Management System is a comprehensive application designed to streamline the operations of a pharmacy. It manages drug inventory, sales transactions, supplier information, and more. The application is built using JavaFX for the user interface and utilizes a relational database for data storage.

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Team](#team)


## Tech Stack

- **Java**: Core programming language for the application.
- **JavaFX**: Used for building the user interface.
- **Maven**: Dependency management and build tool.
- **PostgreSQL**: Relational database for storing data.
- **FXML**: XML-based language used to define the UI components.


## Features

- **Drug Management**: Add, update, and delete drug information.
- **Purchase History**: Record and view purchase transactions.
- **Supplier Management**: Manage supplier information.
- **Inventory Management**: Track drug quantities and alert when stock is low.
- **User Interface**: User-friendly interface built with JavaFX.

## Installation

### Prerequisites

- Java JDK 11 or later
- JavaFX SDK
- Maven
- A relational database (e.g., MySQL)

### Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/pharmacy-management-system.git


   ```
2. **Navigate to the project directory**

```bash
cd pharmacy-management-system
```
3. **Install dependencies**

```bash
mvn clean install
```

4. **Set up the database**

Create a new database and run the provided SQL scripts to set up the tables.

5. **Configure database connection**

Update the database connection settings in the ``DatabaseUtility`` class.

6. **Run the application**

```bash
mvn javafx:run
```

## Usage
Dashboard: Navigate through the dashboard to manage drugs, suppliers, and view purchase history.
Add Purchase: Record new purchase transactions by filling in the required details.
View Reports: Generate and view reports on drug sales and inventory levels.



## Team
- Twum-Antwi Naa Dromo Vanecia -10990540 
- Patricia Ama Mensah - 10892084
- William Boakye - 10996012
- Prince Akpaloo - 10974643
- Marie-Pearl Akoto - 10947903
- Amediku David Kisseh - 10988003
- Justice Tetteh - 10981523

