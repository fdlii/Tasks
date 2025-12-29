package com.task_11_3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private Properties props;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static Connection instance;

    private ConnectionManager() {
        try {
            props = new Properties();
            props.load(new FileReader("src/main/java/com/task_11_3/config/DBConfig.properties"));
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Connection getConnection() {
        try {
            if (instance == null) {
                new ConnectionManager();
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        }
        catch (SQLException ex) {
            System.out.println("Не удалось подключиться к базе данных.");
        }
        return instance;
    }
}
