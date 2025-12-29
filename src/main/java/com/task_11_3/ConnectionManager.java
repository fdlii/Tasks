package com.task_11_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/book_store";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static Connection instance;

    private ConnectionManager(){}

    public static Connection getConnection() {
        try {
            if (instance == null) {
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        }
        catch (SQLException ex) {
            System.out.println("Не удалось подключиться к базе данных.");
        }
        return instance;
    }
}
