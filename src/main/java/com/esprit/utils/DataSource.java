package com.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static Connection connection;  // Make connection static
    private static DataSource instance;

    private DataSource() {
        try {
            String URL = "jdbc:mysql://localhost:3306/eventopia";
            String USERNAME = "root";
            String PASSWORD = "";
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataSource getInstance() {
        if (instance == null)
            instance = new DataSource();
        return instance;
    }

    public static Connection getConnection() {
        return connection;  // Return static connection
    }
}
