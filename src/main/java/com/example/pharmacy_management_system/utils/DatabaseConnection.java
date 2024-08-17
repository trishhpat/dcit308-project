package com.example.pharmacy_management_system.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://dpg-cqkbhm88fa8c73cjpi40-a.oregon-postgres.render.com/pms_j0vq";
    private static final String USER = "pms_j0vq_user";
    private static final String PASSWORD = "uTCiHez285IwnRsWfMIuJ9ehlfVQ4jFh";




    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
