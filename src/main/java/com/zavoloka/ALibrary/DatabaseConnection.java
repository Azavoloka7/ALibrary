package com.zavoloka.ALibrary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.*;
import java.util.Arrays;
@Getter
@Setter

public class DatabaseConnection {


    private Object[] loadedData; // Array to save loaded database records

    // Replace these with your actual database credentials
    private String url = "jdbc:mysql://localhost:3308/ALibrary";
    private String username = "root";
    private String password = "Z@v010ka";

    public DatabaseConnection() {
        // Initialize the array when the class is instantiated
        loadedData = loadDatabase();
    }

    private Object[] loadDatabase() {
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM books");

                // Convert ResultSet to an array
                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    Object[] record = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        record[i - 1] = resultSet.getObject(i);
                    }
                    // Add the record to the array or a data structure of your choice
                    // For simplicity, I'll just print the record here
                    System.out.println("Loaded Record: " + Arrays.toString(record));
                }

                resultSet.close();
                statement.close();
                connection.close();
                System.out.println("Connection closed");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // You can return the array or any other data structure as needed
        return new Object[0];
    }

    // Other methods...

    public static void main(String[] args) {
        // Create an instance of DatabaseConnection
        DatabaseConnection dbConnection = new DatabaseConnection();
        // Access the loaded data array
        Object[] loadedData = dbConnection.getLoadedData();


    }
}
