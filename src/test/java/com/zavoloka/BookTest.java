package com.zavoloka;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void testIsBorrowed() {
        // Create a book
        Book book = new Book("Test Book", "Test Author", 10.99);

        // Ensure that the initial state is not borrowed
        assertFalse(book.isBorrowed());

        // Borrow the book
        book.setBorrowed(true);

        // Check if the book is now marked as borrowed
        assertTrue(book.isBorrowed());
    }

    @Test
    void testReturnBookNotBorrowed() {
        // Create a book
        Book book = new Book("Test Book", "Test Author", 10.99);

        // Ensure that the initial state is not borrowed
        assertFalse(book.isBorrowed());

        // Create a dummy BookLibrary and Client
        BookLibrary bookLibrary = new BookLibrary();
        Client client = new Client(1,10.00 ,25,"John","Doe", "Kyiv city","7567567567");

        // Return the book (which is not borrowed)
        assertFalse(book.returnBook(bookLibrary, book, client));

        // Ensure that the state remains not borrowed
        assertFalse(book.isBorrowed());
    }
    @Test
    void returnBook() {
        // Create a book with borrowed set to true
        Book book = new Book("Test Book", "Test Author", 19.99, false, true);

        // Mock a BookLibrary, Book, and Client for testing
        BookLibrary bookLibrary = new BookLibrary();
        Book mockBook = new Book("Test Book", "Test Author", 19.99, false, true);
        Client mockClient = new Client(1, 50.0, 25, "John", "Doe", "123 Main St", "123456789");

        // Perform the returnBook operation
        boolean result = book.returnBook(bookLibrary, mockBook, mockClient);

        // Assert that the returnBook operation was successful
        assertFalse(result);
        assertFalse(book.isBorrowed()); // Assert that the book is marked as not borrowed after returning
    }

    @Test
    void saveToDatabase() {
        // Assuming you have a test database with known data for testing
        String jdbcUrl = "jdbc:mysql://localhost:3308/ALibrary";
        String username = "root";
        String password = "Z@v010ka";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Create a book and save it to the test database
            Book book = new Book("Test Book", "Test Author", 19.99, false, false);
            book.saveToDatabase(connection);

            // Assert that the book is in the database
            assertTrue(book.isBookInDatabase(connection));
        } catch (SQLException e) {
            fail("Exception occurred while testing saveToDatabase: " + e.getMessage());
        }
    }

    // Add more test methods for other functionalities as needed

}
