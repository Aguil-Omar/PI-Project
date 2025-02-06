package com.Materiels.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSources {
    private Connection connection;
    private static DataSources instance;

    private final String URL = "jdbc:mysql://localhost:3306/pi";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DataSources() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataSources getInstance() {
        if(instance == null)
            instance = new DataSources();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
