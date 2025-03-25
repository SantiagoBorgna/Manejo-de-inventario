# Inventory and Sales Management App

## Overview
Inventory and Sales Management App is a **Java Swing desktop application** designed to streamline **inventory tracking and sales management** across multiple store branches. The system provides **real-time product updates**, **user authentication with different access levels**, and a **MySQL database** for efficient data management.

## Features
- **Multi-branch inventory management** with real-time synchronization.
- **User authentication system** with different access levels:
- **Graphical user interface (GUI)** built with Java Swing.
- **Product details with images** displayed in the interface.
- **Secure database connectivity** using JDBC.
- **Efficient data storage and retrieval** with MySQL.

## Technologies Used
- **Java (Swing)** – Frontend and backend development.
- **MySQL** – Database management.
- **JDBC** – Database connection handling.
- **Git** – Version control.

## Installation
### Prerequisites
Ensure you have the following installed on your system:
- Java JDK 8 or later
- MySQL Server
- MySQL Connector/J (JDBC driver)

### Steps to Set Up Locally
1. **Clone the repository**
   ```sh
   git clone https://github.com/SantiagoBorgna/Manejo-de-inventario.git
   cd Manejo-de-inventario
   ```
2. **Set up the MySQL database**
   - Create a database named `inventory_db`.
   - Import the provided `database_schema.sql` file into MySQL.
3. **Configure database connection**
   - Open `DatabaseConnection.java`.
   - Modify the URL, username, and password to match your MySQL credentials:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
     private static final String USER = "your_mysql_user";
     private static final String PASSWORD = "your_mysql_password";
     ```
4. **Compile and run the application**
   - Using an IDE (IntelliJ, Eclipse, or NetBeans) or command line:
     ```sh
     javac -cp .:mysql-connector-java-8.0.33.jar Main.java
     java -cp .:mysql-connector-java-8.0.33.jar Main
     ```

## Usage
- Launch the application and log in with the provided credentials.
- Navigate through the UI to **add, edit, or remove products**.
- View and manage inventory levels **per branch**.
- Track sales and stock in real-time.

## Contributing
If you’d like to contribute, feel free to fork the repository and submit a pull request with your improvements.

## License
This project is licensed under the **MIT License**. See the LICENSE file for more details.

## Contact
For any inquiries, feel free to reach out via GitHub or email at **santiborgna5@gmail.com**.
