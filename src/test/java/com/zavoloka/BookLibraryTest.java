package com.zavoloka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookLibraryTest {
    private BookLibrary library;

    @BeforeEach
    void setUp() {
        library = BookLibrary.initializeLibrary();
    }

    @Test
    void testBorrowBook() {
        // Select a book from the library
        Book bookToBorrow = library.getAllBooks().get(0);

        // Create a client for borrowing
        Client client = new Client(1,10.00 ,25,"John","Doe", "Kyiv city","7567567567");

        // Borrow the book
        assertTrue(library.borrowBook(bookToBorrow, client));

        // Check if the book is in the borrowed list
        assertTrue(library.getBorrowedBooks().contains(bookToBorrow));
    }

    @Test
    void testReturnBook() {
        // Select a book from the library
        Book bookToBorrow = library.getAllBooks().get(0);

        // Create a client for borrowing
        Client client = new Client(1,10.00 ,25,"John","Doe", "Kyiv city","7567567567");

        // Borrow the book
        library.borrowBook(bookToBorrow, client);

        // Return the book
        library.returnBook(bookToBorrow);

        // Check if the book is back in the library
        assertTrue(library.getAllBooks().contains(bookToBorrow));
        assertFalse(library.getBorrowedBooks().contains(bookToBorrow));
    }

    // Add more test methods to cover other functionalities of your library

    @Test
    void testLibraryInitialization() {
        // Check if the library is initialized with the correct number of books
        assertEquals(20, library.getAllBooks().size());
    }
}
