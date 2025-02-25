
package com.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private Connection con;
    private static DataSource ds;
    private final String URL = "jdbc:mysql://localhost:3306/eventopia";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DataSource() {
        try {
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventopia", "root", "");
            System.out.println("Connection established");
        } catch (SQLException var2) {
            SQLException e = var2;
            System.out.println(e.getMessage());
        }

    }

    public static DataSource getInstance() {
        if (ds == null) {
            ds = new DataSource();
        }

        return ds;
    }

    public Connection getConnection() {
        return this.con;
    }
}
