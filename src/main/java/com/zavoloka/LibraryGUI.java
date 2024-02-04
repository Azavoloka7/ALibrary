package com.zavoloka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryGUI extends JFrame {
    private final BookLibrary bookLibrary;

    public LibraryGUI(BookLibrary bookLibrary) {
        this.bookLibrary = bookLibrary;

        setTitle("Library Application");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();
    }

    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton borrowButton = new JButton("Borrow a Book");
        JButton returnButton = new JButton("Return a Book");
        JButton displayButton = new JButton("Display All Books");

        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBookDialog();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBookDialog();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllBooks();
            }
        });

        panel.add(borrowButton);
        panel.add(returnButton);
        panel.add(displayButton);

        getContentPane().add(panel);
    }

    private void returnBookDialog() {
        // Create a dialog to input book details
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Return a Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Retrieve input values
            String title = titleField.getText();
            String author = authorField.getText();

            // Find the book in the library based on input details
            Book bookToReturn = findBookInLibrary(title, author);

            if (bookToReturn != null) {
                // Check if the book is borrowed
                if (bookToReturn.isBorrowed()) {
                    // Retrieve the client who borrowed the book
                    Client client = findBorrowingClient(bookToReturn);

                    if (client != null) {
                        // Return the book
                        client.returnBook(bookLibrary, bookToReturn);

                        // Add the returned book back to the library
                        bookLibrary.returnBook(bookToReturn);

                        JOptionPane.showMessageDialog(this, "Book returned successfully!", "Return Book", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: Unable to find borrowing client.", "Return Book", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Book is not currently borrowed.", "Return Book", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Book not found in the library.", "Return Book", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Client findBorrowingClient(Book book) {
        // Iterate through all clients to find the one who borrowed the book
        for (Client client : bookLibrary.getAllClients("jdbc:mysql://localhost:3308/ALibrary", "root", "Z@v010ka" )) {
            if (client.getOwnedBooks().contains(book)) {
                return client;
            }
        }
        return null;
    }

    private void borrowBookDialog() {
        // Create a dialog to input book details
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Borrow a Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Retrieve input values
            String title = titleField.getText();
            String author = authorField.getText();

            // Find the book in the library based on input details
            Book bookToBorrow = findBookInLibrary(title, author);

            if (bookToBorrow != null) {
                // Create a dummy Client for borrowing
                Client client = new Client(1, 10.00, 25, "John", "Doe", "Kyiv city", "7567567567");

                // Borrow the book
                boolean success = bookToBorrow.borrowBook(bookLibrary, client);


                if (success) {
                    JOptionPane.showMessageDialog(this, "Book borrowed successfully!", "Borrow Book", JOptionPane.INFORMATION_MESSAGE);
                    bookToBorrow.setBorrowed(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not available for borrowing.", "Borrow Book", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Book not found in the library.", "Borrow Book", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Book findBookInLibrary(String title, String author) {
        for (Book book : bookLibrary.getAllBooks()) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                return book;
            }
        }
        return null;
    }

    private void displayAllBooks() {
        JTextArea bookTextArea = new JTextArea();
        bookTextArea.setEditable(false);

        // Append id, title, and author of each book to the JTextArea
        for (Book book : bookLibrary.getAllBooks()) {
            bookTextArea.append("ID: " + book.getId() + "\n");
            bookTextArea.append("Title: " + book.getTitle() + "\n");
            bookTextArea.append("Author: " + book.getAuthor() + "\n");
            bookTextArea.append("\n"); // Add a blank line for better readability
        }

        JScrollPane scrollPane = new JScrollPane(bookTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, 800));

        JOptionPane.showMessageDialog(this, scrollPane, "Library Books", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookLibrary bookLibrary = LibraryApplication.initializeLibraryFromDatabase(
                    "jdbc:mysql://localhost:3308/ALibrary", "root", "Z@v010ka");

            LibraryGUI libraryGUI = new LibraryGUI(bookLibrary);
            libraryGUI.setVisible(true);
        });
    }
}
