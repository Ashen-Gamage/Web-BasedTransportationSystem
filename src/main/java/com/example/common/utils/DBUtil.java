package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=transport_app;"
                    + "encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";    // <-- change
    private static final String PASSWORD = "AshenGeeth"; // <-- change

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
