package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=TransportDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "TransportDB";           // SQL Server login
    private static final String PASSWORD = "Transport123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
