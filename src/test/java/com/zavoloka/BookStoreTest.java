package com.zavoloka;

import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookStoreTest {

    @Test
    void testAddBook() {
        BookStore bookStore = new BookStore();
        Book book = new Book( "Test Book", "Test Author", 9.99, false,false);

        bookStore.addBook(book);

        assertTrue(bookStore.getAllBooks().contains(book));
    }

    @Test
    void testSellBook() {
        BookStore bookStore = new BookStore();
        Manager manager = new Manager();
        Client client = new Client(666, 20.00, 123, "John", "Doe", "456 Main St", "987654321");

        Book bookToSell = new Book( "Test Book", "Test Author", 10.00, false,false);

        bookStore.addBook(bookToSell);

        bookStore.sellBook(manager, bookToSell, client);

        assertTrue(bookToSell.isSold());
        assertEquals(1, manager.getSales());
        assertEquals(10.00, client.getBalance(), 0.01);
        assertEquals(0, bookStore.getAllBooks().size());
    }
}

