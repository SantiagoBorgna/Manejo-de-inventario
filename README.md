# El Arca Home - Management System

Desktop application developed in Java Swing with a connection to MySQL, designed to facilitate the commercial management of "El Arca Home" stores.

The system allows for organizing and tracking inventory, sales, and web orders efficiently across different branches.

---

## Features

- Multi-Branch Inventory: Stock management and synchronization for different store locations.
- Sales & Billing: Processing of mixed payment methods and automatic PDF receipt generation.
- Web Orders Management: Specialized interfaces for "In-Store Pickup" and "Home Delivery" with real-time status toggling.
- Financial Control: Daily ledger (Libro Diario) for net profit calculation, Customer Current Accounts, and Credit Notes tracking.
- Role-Based Access: Distinct interfaces and permissions based on user roles (Superadmin, Admin, Employee).

---

## Tech Stack

- Java Swing → Graphical User Interface.
- MySQL → Relational database (hosted on AWS RDS).
- JDBC → Connection between Java and MySQL.
- External Libraries → iTextPDF for automated document generation, JCalendar for date picking, and Imgur API for cloud image hosting.

---

## Installation and Execution

1. Clone the repository:
  ```bash
  git clone https://github.com/SantiagoBorgna/Manejo-de-inventario.git
  ```
2. Create the MySQL database and the necessary tables.
3. Configure the database connection. Create a config.properties file in the root directory.
  ```bash
  db.url=jdbc:mysql://[YOUR_DB_HOST]:3306/arcadb
  db.user=your_username
  db.password=your_password
  ```
4. Ensure all external .jar dependencies are properly loaded in the lib/ directory and added to your IDE's project structure.
5. Compile and run the main class or execute the compiled .jar file:
  ```bash
  java -jar SistemaArca.jar
  ```

---

## Screenshots

<img width="1919" height="1137" alt="arca-inventory" src="https://github.com/user-attachments/assets/f67d5823-972d-4f42-9e2b-f146394aa5a9" />

<img width="1919" height="1142" alt="arca-ventas" src="https://github.com/user-attachments/assets/9aa8989c-da1e-43e1-9fdb-6fdde8331150" />

<img width="1919" height="1141" alt="arca-libro-diario" src="https://github.com/user-attachments/assets/ffc406e8-ddfb-4398-905f-3a9803379e2f" />

---

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

## Contact
You can reach me via GitHub or email at **santiborgna5@gmail.com**.
