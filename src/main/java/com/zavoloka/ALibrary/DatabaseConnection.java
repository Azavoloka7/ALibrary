package com.zavoloka.ALibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseConnection {

    // Replace these with your actual database credentials
    private String url = "jdbc:mysql://localhost:3308/ALibrary";
    private String username = "root";
    private String password = "Z@v010ka";

    public DatabaseConnection() {
        // Initialize the array when the class is instantiated
        List<Object[]> loadedData = loadDatabase();
        // Access the loaded data as needed
    }

    private List<Object[]> loadDatabase() {
        List<Object[]> loadedData = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 Statement statement = connection.createStatement()) {

                ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

                // Convert ResultSet to a list of arrays
                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    Object[] record = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        record[i - 1] = resultSet.getObject(i);
                    }
                    // Add the record to the list
                    loadedData.add(record);
                    // For simplicity, you can print the record here if needed
                    System.out.println("Loaded Record: " + Arrays.toString(record));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // You can return the list or any other data structure as needed
        return loadedData;
    }

    // Other methods...

    public static void main(String[] args) {
        // Create an instance of DatabaseConnection
        DatabaseConnection dbConnection = new DatabaseConnection();
    }
}
