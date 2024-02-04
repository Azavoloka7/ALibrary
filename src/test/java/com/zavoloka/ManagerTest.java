package com.zavoloka;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {



    @Test
    void testIncrementSalesCount() {
        // Create a Manager
        Manager manager = new Manager(1, 30, "John", "Doe", 0, 2);

        // Increment sales count
        manager.incrementSalesCount();

        // Check if the sales count is incremented
        assertEquals(1, manager.getSalesCount());
    }

    @Test
    void testIncrementSales() {
        // Create a Manager
        Manager manager = new Manager(1, 30, "John", "Doe", 0, 2);

        // Increment sales
        manager.incrementSales();

        // Check if sales are incremented
        assertEquals(1, manager.getSales());
    }

    @Test
    void testToString() {
        // Create a Manager
        Manager manager = new Manager(1, 30, "John", "Doe", 10, 2);

        // Create a Book
        Book book = new Book( "Test Book", "Test Author", 19.99, false,false);

        // Add the book to the manager's list of all books
        manager.getAllBooks().add(book);

        // Check the string representation of the Manager
        assertEquals("Manager{id=1, age=30, firstName='John', lastName='Doe', sales=10, experience=2, salesCount=0, allBooks=[" + book.toString() + "]}", manager.toString());
    }


}
