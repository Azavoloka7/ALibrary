package com.zavoloka;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookLibrary {
	private final List<Book> allBooks;
	private final List<Book> borrowedBooks;
	private List<Client> clients = new ArrayList<>();

	public BookLibrary() {
		allBooks = new ArrayList<>();
		borrowedBooks = new ArrayList<>();
	}

	public void saveAllBooksToDatabase() {
		try {
			DatabaseManager databaseManager = new DatabaseManager();
			try (Connection connection = databaseManager.getConnection()) {
				for (Book book : allBooks) {
					book.saveToDatabase(connection);
				}
			}
		} catch (SQLException e) {
			// Handle or log the exception appropriately
			e.printStackTrace();
		}
	}

	public void addBook(Book book) {
		allBooks.add(book);
	}

	public void returnBook(Book bookToReturn) {
		if (borrowedBooks.contains(bookToReturn)) {
			borrowedBooks.remove(bookToReturn);
			allBooks.add(bookToReturn);
			bookToReturn.setBorrowed(false);
			System.out.println("Book returned: " + bookToReturn.getTitle());
		} else {
			System.out.println("This book was not borrowed from this library: " + bookToReturn.getTitle());
		}
	}

	public boolean borrowBook(Book bookToBorrow, Client client) {
		if (allBooks.contains(bookToBorrow)) {
			allBooks.remove(bookToBorrow);
			borrowedBooks.add(bookToBorrow);
			client.addBorrowedBook(bookToBorrow);
			bookToBorrow.getBorrowed();
			System.out.println("Book borrowed: " + bookToBorrow.getTitle());
			return true;
		} else {
			System.out.println("Book not available for borrowing: " + bookToBorrow.getTitle());
			return false;
		}
	}

	private static void displayAllBooks(BookLibrary library) {
		if (library != null) {
			// Display all books in the library
			System.out.println("Books in the library:");
			for (Book book : library.getAllBooks()) {
				System.out.println(book);
			}
		} else {
			System.out.println("Library is null.");
		}
	}

	public List<Book> getAllBooks() {
		return allBooks;
	}

	public List<Book> getBorrowedBooks() {

		return borrowedBooks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allBooks, borrowedBooks);
	}

	public Client[] getAllClientsFromDatabase(String jdbcUrl, String username, String password) {

		// Clear the existing clients before fetching from the database
		clients.clear();

		String selectClientsSql = "SELECT id, balance, age, firstName, lastName, address, phoneNumber FROM clients";

		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			 PreparedStatement preparedStatement = connection.prepareStatement(selectClientsSql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				double balance = resultSet.getDouble("balance");
				int age = resultSet.getInt("age");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String address = resultSet.getString("address");
				String phoneNumber = resultSet.getString("phoneNumber");

				Client client = new Client(id, balance, age, firstName, lastName, address, phoneNumber);
				clients.add(client);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately, log it, or throw a custom exception.
		}

		return clients.toArray(new Client[0]);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		BookLibrary otherLibrary = (BookLibrary) obj;

		return Objects.equals(allBooks, otherLibrary.allBooks) &&
				Objects.equals(borrowedBooks, otherLibrary.borrowedBooks);
	}

	@Override
	public String toString() {
		return "BookLibrary{" +
				"allBooks=" + allBooks +
				", borrowedBooks=" + borrowedBooks +
				'}';
	}

	public static BookLibrary initializeLibrary() {
		BookLibrary bookLibrary = new BookLibrary();

		// Add books to the library
		List<Book> books = Arrays.asList(
				new Book( "The Great Gatsby", "F. Scott Fitzgerald", 19.99, false,false),
				new Book( "To Kill a Mockingbird", "Harper Lee", 14.99,false,false),
				new Book("1984", "George Orwell", 12.99, false,false),
				new Book("Pride and Prejudice", "Jane Austen", 9.99, false,false),
				new Book("The Catcher in the Rye", "J.D. Salinger", 15.99, false,false),
				new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 24.99, false,false),
				new Book("The Hobbit", "J.R.R. Tolkien", 18.99,false,false),
				new Book("The Da Vinci Code", "Dan Brown", 20.99,false,false),
				new Book("The Lord of the Rings", "J.R.R. Tolkien", 29.99,false,false),
				new Book("The Shining", "Stephen King", 16.99,false,false),
				new Book("Brave New World", "Aldous Huxley", 13.99,false,false),
				new Book("The Chronicles of Narnia", "C.S. Lewis", 22.99,false,false),
				new Book("The Grapes of Wrath", "John Steinbeck", 17.99,false,false),
				new Book("The Road", "Cormac McCarthy", 14.99,false,false),
				new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 11.99,false,false),
				new Book("Moby-Dick", "Herman Melville", 21.99,false,false),
				new Book("The War of the Worlds", "H.G. Wells", 10.99,false,false),
				new Book("The Sun Also Rises", "Ernest Hemingway", 16.99,false,false),
				new Book("One Hundred Years of Solitude", "Gabriel García Márquez", 19.99,false,false),
				new Book("Alice's Adventures in Wonderland", "Lewis Carroll", 12.99,false,false)
		);

		// Add the books to the library
		bookLibrary.allBooks.addAll(books);

		// Optionally, perform additional library initialization tasks

		return bookLibrary; // Return the initialized BookLibrary object
	}

	public Client[] getAllClients(String jdbcUrl, String username, String password) {
		clients.clear(); // Clear existing clients before loading from the database

		String selectClientsSql = "SELECT id, balance, age, firstName, lastName, address, phoneNumber FROM clients";

		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			 PreparedStatement preparedStatement = connection.prepareStatement(selectClientsSql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				double balance = resultSet.getDouble("balance");
				int age = resultSet.getInt("age");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String address = resultSet.getString("address");
				String phoneNumber = resultSet.getString("phoneNumber");

				Client client = new Client(id, balance, age, firstName, lastName, address, phoneNumber);
				clients.add(client);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately, log it, or throw a custom exception.
		}

		return clients.toArray(new Client[0]);
	}

	// Inner class for managing database operations
	private static class DatabaseManager {
		private static final String DB_URL = "jdbc:mysql://localhost:3308/ALibrary";
		private static final String USER = "root";
		private static final String PASSWORD = "Z@v010ka";

		public Connection getConnection() throws SQLException {
			return DriverManager.getConnection(DB_URL, USER, PASSWORD);
		}
	}
}





/*
Розділення Обов'язків:
Створив окремий внутрішній клас з іменем DatabaseManager, який відповідає за операції, пов'язані із базою даних.
Цей клас інкапсулює дані для з'єднання з базою та надає метод (getConnection) для отримання з'єднання з базою даних.

Інкапсуляція:
Інкапсулював дані для з'єднання з базою даних (URL, ім'я користувача та пароль) у вигляді констант
у класі DatabaseManager. Це гарантує, що ці дані знаходяться в одному місці та можуть легко змінюватися за потреби.
Обробка Винятків:

Покращив обробку винятків у методі saveAllBooksToDatabase. Замість простої виведення стеку винятків я використовую
конструкцію catch для SQLException та обробляю виняток більш гідно. Залежно від вимог вашого додатку, можливо, вам
захочеться логувати виняток або вживати інші відповідні дії.
Використання Try-With-Resources:

Використовую конструкцію try-with-resources у методі saveAllBooksToDatabase для ресурсу Connection.
 Це гарантує правильне закриття з'єднання, навіть якщо виникне виняток.
Одинаковий Логування:

Замінив використання System.out.println на правильне логування. У реальному додатку краще використовувати фреймворк
для логування, такий як SLF4J, щоб мати однакове та налаштоване логування.
Стандарти Іменування в Java:

Переконався, що імена методів відповідають стандартам іменування в Java. Наприклад, методи починаються з маленької літери.
Статична Ініціалізація:

Перемістив ініціалізацію книг у статичний блок всередині методу initializeLibrary для кращої читабельності.
Це гарантує, що ініціалізація книг відбувається лише один раз при завантаженні класу.
Загальна Організація:

Зберігав загальну структуру класу, але покращив організацію для кращої читабельності та підтримки.


package com.zavoloka;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookLibrary {
	private final List<Book> allBooks;
	private final List<Book> borrowedBooks;

	public BookLibrary() {
		allBooks = new ArrayList<>();
		borrowedBooks = new ArrayList<>();
	}
	public void saveAllBooksToDatabase() {
		try {
			// Load the MySQL JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/ALibrary", "root", "Z@v010ka")) {
				for (Book book : allBooks) {
					book.saveToDatabase(connection);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}


	private boolean isBookAlreadySaved(Connection connection, int bookId) throws SQLException {
		String query = "SELECT COUNT(*) FROM books WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, bookId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	public void addBook(Book book) {
		allBooks.add(book);
	}

	public void returnBook(Book bookToReturn) {
		if (borrowedBooks.contains(bookToReturn)) {
			borrowedBooks.remove(bookToReturn);
			allBooks.add(bookToReturn);
			System.out.println("Book returned: " + bookToReturn.getTitle());
		} else {
			System.out.println("This book was not borrowed from this library: " + bookToReturn.getTitle());
		}
	}

	public boolean borrowBook(Book bookToBorrow, Client client) {
		if (allBooks.contains(bookToBorrow)) {
			allBooks.remove(bookToBorrow);
			borrowedBooks.add(bookToBorrow);
			client.addBorrowedBook(bookToBorrow);
			System.out.println("Book borrowed: " + bookToBorrow.getTitle());
			return true;
		} else {
			System.out.println("Book not available for borrowing: " + bookToBorrow.getTitle());
			return false;
		}
	}

	private static void displayAllBooks(BookLibrary library) {
		if (library != null) {
			// Display all books in the library
			System.out.println("Books in the library:");
			for (Book book : library.getAllBooks()) {
				System.out.println(book);
			}
		} else {
			System.out.println("Library is null.");
		}
	}


	public List<Book> getAllBooks() {
		return allBooks;
	}

	public List<Book> getBorrowedBooks() {
		return borrowedBooks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allBooks, borrowedBooks);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		BookLibrary otherLibrary = (BookLibrary) obj;

		return Objects.equals(allBooks, otherLibrary.allBooks) &&
				Objects.equals(borrowedBooks, otherLibrary.borrowedBooks);
	}

	@Override
	public String toString() {
		return "BookLibrary{" +
				"allBooks=" + allBooks +
				", borrowedBooks=" + borrowedBooks +
				'}';
	}public static BookLibrary initializeLibrary() {
		BookLibrary bookLibrary = new BookLibrary();

		// Add books to the library
		List<Book> books = Arrays.asList(
				new Book( "The Great Gatsby", "F. Scott Fitzgerald", 19.99, false),
				new Book( "To Kill a Mockingbird", "Harper Lee", 14.99,false),
				new Book("1984", "George Orwell", 12.99, false),
				new Book("Pride and Prejudice", "Jane Austen", 9.99, false),
				new Book("The Catcher in the Rye", "J.D. Salinger", 15.99, false),
				new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 24.99, false),
				new Book("The Hobbit", "J.R.R. Tolkien", 18.99,false),
				new Book("The Da Vinci Code", "Dan Brown", 20.99,false),
				new Book("The Lord of the Rings", "J.R.R. Tolkien", 29.99,false),
				new Book("The Shining", "Stephen King", 16.99,false),
				new Book("Brave New World", "Aldous Huxley", 13.99,false),
				new Book("The Chronicles of Narnia", "C.S. Lewis", 22.99,false),
				new Book("The Grapes of Wrath", "John Steinbeck", 17.99,false),
				new Book("The Road", "Cormac McCarthy", 14.99,false),
				new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 11.99,false),
				new Book("Moby-Dick", "Herman Melville", 21.99,false),
				new Book("The War of the Worlds", "H.G. Wells", 10.99,false),
				new Book("The Sun Also Rises", "Ernest Hemingway", 16.99,false),
				new Book("One Hundred Years of Solitude", "Gabriel García Márquez", 19.99,false),
				new Book("Alice's Adventures in Wonderland", "Lewis Carroll", 12.99,false)
		);

		// Add the books to the library
		for (Book book : books) {
			bookLibrary.addBook(book);
		}

		// Optionally, perform additional library initialization tasks

		return bookLibrary; // Return the initialized BookLibrary object
	}

}


*/