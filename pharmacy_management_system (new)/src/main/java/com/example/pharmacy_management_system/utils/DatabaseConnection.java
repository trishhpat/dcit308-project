package com.example.pharmacy_management_system.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://dpg-cpnubig8fa8c73b7h500-a.oregon-postgres.render.com/pms_database_v9a2";
    private static final String USER = "pms_database_v9a2_user";
    private static final String PASSWORD = "r3UcCBXBQ9umX0L2c96E2BoYRHsJzR6a";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
