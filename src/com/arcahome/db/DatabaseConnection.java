package com.arcahome.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream("config.properties");
                props.load(fis);

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, pass);

            } catch (IOException e) {
                throw new SQLException("No se pudo leer el archivo config.properties", e);
            }
        }
        return connection;
    }
}
//static String url = "jdbc:mysql://127.0.0.1:3306/arcadb";
//static String usuario = "santi";
//static String contrasena = "";