package com.zavoloka;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookStore {
    private final Manager manager;
    private final List<Book> allBooks;

    public BookStore() {
        this.allBooks = new ArrayList<>();
        this.manager = new Manager();
        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        String jdbcUrl = "jdbc:mysql://localhost:3308/ALibrary";
        String username = "root";
        String password = "Z@v010ka";

        String query = "SELECT * FROM books";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                double price = resultSet.getDouble("price");
                boolean sold = resultSet.getBoolean("sold");
                boolean borrowed = resultSet.getBoolean("borrowed");
                Book book = new Book( title, author, price, sold, borrowed);
                allBooks.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Other methods remain unchanged



    void sellBook(Manager manager, Book book, Client client) {
        if (isBookAvailableForSale(book) && isClientBalanceSufficient(client, book)) {
            processBookSale(book, client);
            manager.incrementSales();
        } else {
            System.out.println("Book not available for sale: " + book.getTitle());
        }
    }

    public boolean isClientBalanceSufficient(Client client, Book book) {
        // Check if the client's balance is sufficient for the book purchase
        return client.getBalance() >= book.getPrice();
    }

    public boolean isBookAvailableForSale(Book book) {
        // Check if the book is available for sale (not borrowed)
        return !book.isBorrowed();
    }

    private void processBookSale(Book book, Client client) {
        try {
            // Deduct the book price from the client's balance
            client.makePayment(book.getPrice());

            // Set the book as sold in the database
            String updateQuery = "UPDATE books SET sold = ? WHERE id = ?";
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/ALibrary", "root" , "Z@v010ka");
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setBoolean(1, true);
                updateStatement.setInt(2, book.getId());
                updateStatement.executeUpdate();
            }

            // Remove the sold book from the local book list
            allBooks.remove(book);

            // Notify the client about the purchase
            client.buyBook(manager, book);
            System.out.println("Book sold: " + book.getTitle());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        try {
            // Insert the book into the database
            String insertQuery = "INSERT INTO books (id, title, author, price, sold) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/ALibrary", "root" , "Z@v010ka");
                 PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

                insertStatement.setInt(1, book.getId());
                insertStatement.setString(2, book.getTitle());
                insertStatement.setString(3, book.getAuthor());
                insertStatement.setDouble(4, book.getPrice());
                insertStatement.setBoolean(5, book.isSold());
                insertStatement.executeUpdate();
            }

            // Add the book to the local book list
            allBooks.add(book);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Object> getAllBooks() {
        Collection<Object> allBooksData = new ArrayList<>();

        try {
            // Retrieve all books from the database
            String selectQuery = "SELECT * FROM books";
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/ALibrary", "root" , "Z@v010ka");
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectQuery)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    double price = resultSet.getDouble("price");
                    boolean sold = resultSet.getBoolean("sold");
                    boolean borrowed = resultSet.getBoolean("borrowed");

                    // Create a Book object with the retrieved data
                    Book book = new Book( title, author, price, sold, borrowed);

                    // Add the Book object to the collection
                    allBooksData.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allBooksData;
    }
    public static void main(String[] args) {
        // Creating instances of Manager and Client
        Manager manager = new Manager(9, 29, "Alexei", "Nesterets", 0, 1);
        Client client = new Client(8, 50.00, 34, "Volodymyr", "Potaichuk", "123 Main St", "0990999");

        // Creating an instance of BookStore
        BookStore bookStore = new BookStore();

        // Selling a book to the client
        bookStore.sellBook(manager, bookStore.allBooks.get(0), client);
        System.out.println("Книжка в бібліотеці : "+bookStore.allBooks.get(10));
        System.out.println("Куплена книжка : "+client.getOwnedBooks());
        bookStore.sellBook(manager, bookStore.allBooks.get(1), client);




        // Displaying manager and client information
        System.out.println(manager);
        System.out.println(client.getBalance());
        System.out.println("Remaining books in the store: " + bookStore.allBooks.size());

        //  System.out.println(bookStore.getAllBooks());
    }
}




/*
1.	Метод sellBook підлягав рефакторингу:
•	Метод sellBook був перероблений для поліпшення зрозумілості та інкапсуляції.
•	Замість перевірки кількох умов у одному операторі if, логіку було розбито на окремі приватні методи для полегшення читання та обслуговування.
•	Нові приватні методи (isBookAvailableForSale, isClientBalanceSufficient та processBookSale) відповідають за конкретні аспекти процесу продажу.
2.	Приватні допоміжні методи:
•	isBookAvailableForSale: Перевіряє, чи книга доступна в магазині і не була продана.
•	isClientBalanceSufficient: Перевіряє, чи баланс клієнта достатній для покупки книги.
•	processBookSale: Обробляє фактичний процес продажу, включаючи оновлення балансу клієнта, позначення книги як проданої, видалення її з магазину, сповіщення клієнта та збільшення кількості продажів менеджера.
3.	Оновлення головного методу:
•	Створено екземпляри Manager та Client.
•	Створено екземпляр BookStore.
•	Додано список книг у магазин за допомогою методу addBook.
4.	Коментарі:
•	Додано коментарі для пояснення призначення кожного методу та загальної структури коду.
Рефакторинг спрямований на зроблення коду більш модульним, зрозумілим та підтримуваним. Він розбиває відповідальність на менші, автономні методи, кожен з яких відповідає за конкретну частину функціоналу. Це поліпшує читабельність та дозволяє легше вносити зміни чи додавати новий функціонал у майбутньому




public class BookStore {
    private final Manager manager;
    private final List<Book> allBooks;

    public BookStore() {
        this.allBooks = new ArrayList<>();
        this.manager = new Manager();
    }

    public void addBook(Book book) {
        allBooks.add(book);
    }

    public void sellBook(Manager manager, Book bookToSell, Client client) {
        // Check if the book is available and not sold
        if (allBooks.contains(bookToSell) && !bookToSell.isSold()&&client.getBalance()>=bookToSell.getPrice()) {
            // Mark the book as sold, remove from the store, and notify the client
            client.makePayment(bookToSell.getPrice());
            bookToSell.setSold(true);
            allBooks.remove(bookToSell);
            client.buyBook(manager,bookToSell);
            System.out.println("Book sold: " + bookToSell.getTitle());

            // Increment manager's sales count
            manager.incrementSales();
        } else {
            System.out.println("Book not available for sale: " + bookToSell.getTitle());
        }
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(allBooks); // Return a copy to avoid external modification
    }

    public Manager getManager() {
        return manager;
    }

    public static void main(String[] args) {

        Manager manager = new Manager(9, 29, "Alexei", "Nesterets", 0, 1);
        Manager manager2 = new Manager(41, 32, "Evgenii", "Gombolevskii", 0, 5);
        Client client = new Client(8, 50.00, 34, "Volodymyr", "Potaichuk", "123 Main St", "0990999");
        Client client2 = new Client(10, 200.00, 32, "Ihor", "Bezluda", "13 Ushakov St", "0127745");

        BookStore bookStore = new BookStore();

        List<Book> books = Arrays.asList(
                new Book(11, "The Great Gatsby", "F. Scott Fitzgerald", 19.99, false),
                new Book(12, "To Kill a Mockingbird", "Harper Lee", 14.99,false),
                new Book(13,"1984", "George Orwell", 12.99, false),
                new Book(14,"Pride and Prejudice", "Jane Austen", 9.99, false),
                new Book(15,"The Catcher in the Rye", "J.D. Salinger", 15.99, false),
                new Book(16,"Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 24.99, false),
                new Book(17,"The Hobbit", "J.R.R. Tolkien", 18.99,false),
                new Book(18,"The Da Vinci Code", "Dan Brown", 20.99,false),
                new Book(19,"The Lord of the Rings", "J.R.R. Tolkien", 29.99,false),
                new Book(20,"The Shining", "Stephen King", 16.99,false),
                new Book(21,"Brave New World", "Aldous Huxley", 13.99,false),
                new Book(22,"The Chronicles of Narnia", "C.S. Lewis", 22.99,false),
                new Book(23,"The Grapes of Wrath", "John Steinbeck", 17.99,false),
                new Book(24,"The Road", "Cormac McCarthy", 14.99,false),
                new Book(25,"The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 11.99,false),
                new Book(26,"Moby-Dick", "Herman Melville", 21.99,false),
                new Book(27,"The War of the Worlds", "H.G. Wells", 10.99,false),
                new Book(28,"The Sun Also Rises", "Ernest Hemingway", 16.99,false),
                new Book(29,"One Hundred Years of Solitude", "Gabriel García Márquez", 19.99,false),
                new Book(30,"Alice's Adventures in Wonderland", "Lewis Carroll", 12.99,false)
        );
        for (Book book : books) {
            manager.addBook(bookStore, book);

        }

        bookStore.sellBook(manager2,bookStore.allBooks.get(7),client2);
        bookStore.sellBook(manager2,bookStore.allBooks.get(10),client);

      //  bookStore.sellBook(manager2,bookStore.allBooks.get(15),client2);
       // System.out.println(client.getBalance());
       // System.out.println(client.getBalance());
        // Sell a book to the client

        System.out.println(manager2);
        System.out.println(client);
        System.out.println(bookStore.allBooks.size());
        //System.out.println(client2.getOwnedBooks());
        //System.out.println(client.getOwnedBooks());
        // Display remaining books in the store
     /*   System.out.println("Remaining books in the store:");
        for (Book book : bookStore.getAllBooks()) {
            System.out.println(book.getTitle());
        }*/



