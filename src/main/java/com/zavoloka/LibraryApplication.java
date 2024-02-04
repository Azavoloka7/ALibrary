package com.zavoloka;

import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class LibraryApplication {

    private static void displayAllBooks(@NotNull BookLibrary library) {
        System.out.println("Books in the library:");
        for (Book book : library.getAllBooks()) {
            System.out.println(book);
        }
    }
    

        // Add books to the library
        static BookLibrary initializeLibraryFromDatabase(String jdbcUrl, String username, String password) {
            BookLibrary bookLibrary = new BookLibrary();

            String selectBooksSql = "SELECT title, author, price, sold, borrowed FROM books";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                 PreparedStatement preparedStatement = connection.prepareStatement(selectBooksSql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    double price = resultSet.getDouble("price");
                    boolean sold = resultSet.getBoolean("sold");
                    boolean borrowed = resultSet.getBoolean("borrowed");

                    Book book = new Book(title,  author,  price,  sold, borrowed);
                    bookLibrary.addBook(book);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately, log it, or throw a custom exception.
            }

            return bookLibrary;
        }


    public static void main(String[] args) {
        // Create objects
        BookLibrary bookLibrary = initializeLibraryFromDatabase("jdbc:mysql://localhost:3308/ALibrary", "root", "Z@v010ka");
       // bookLibrary.saveAllBooksToDatabase();
        Manager manager = new Manager(9, 29, "Alexei", "Nesterets", 0, 1);
        Manager manager2 = new Manager(41, 32, "Evgenii", "Gombolevskii", 0, 5);
        Client client = new Client(8, 100.00, 34, "Volodymyr", "Potaichuk", "123 Main St", "0990999");
        Client client2 = new Client(10, 100.00, 32, "Ihor", "Bezluda", "13 Ushakov St", "0127745");


        // Display all books in the library
       // displayAllBooks(bookLibrary);
        System.out.println("Number of books in library : "+bookLibrary.getAllBooks().size());

        client.borrowBook(bookLibrary,bookLibrary.getAllBooks().get(77));

        System.out.println("Книга, шо взяли почитати : "+bookLibrary.getBorrowedBooks().get(0));

        System.out.println("Number of books in library : "+bookLibrary.getAllBooks().size());


        client.returnBook(bookLibrary, bookLibrary.getBorrowedBooks().get(0));


        System.out.println("Книга, шо повернули : "+bookLibrary.getAllBooks().get(99));


        System.out.println("Книжок в бібліотеці = "+bookLibrary.getAllBooks().size());

        System.out.println(manager2);

  

    }

  
}

/*
Пояснення змін:

Ім'я Класу:
Перейменовано ALibrary на LibraryApplication для кращого відображення його призначення.

Головний Метод:
завантажуємо готову бібліотеку з MySQL db окремим методом initializeLibraryFromDatabase.

Створення Об'єктів:
Перенесено створення library, manager, manager2, client, та client2 в метод main для кращої читабельності.

Ініціалізація Менеджера:
Припускаючи, що у класі Manager є конструктор за замовчуванням, створено менеджера за замовчуванням і додано книги до бібліотеки за допомогою методу addBook.

Вилучення Методу:
Вилучено логіку відображення книг у власний метод displayAllBooks для кращої організації коду та зручності читання.

Ці зміни спрямовані на поліпшення читабельності, підтримки та організації коду. Важливо мати чітку структуру та змістовні імена для кращого розуміння та майбутнього розвитку.
 */