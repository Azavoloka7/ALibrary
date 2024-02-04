
package com.zavoloka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Book {
	private static int idCounter = 1;

	private int id;
	private String title;
	private String author;
	private double price;
	private boolean sold;
	private boolean borrowed;

	public Book(String title, String author, double price,boolean sold, boolean borrowed) {
		this.id = idCounter++;
		this.title = title;
		this.author = author;
		this.price = price;
		this.sold = false;
		this.borrowed = false;
	}

	public Book(String title, String author, double price) {
		this.title = title;
		this.author = author;
		this.price = price;
	}
	public void getBorrowed() {
        this.borrowed = true;
    }

	public boolean returnBook(BookLibrary bookLibrary, Book book, Client client) {
		// Check if the book is borrowed by the client
		if (book.isBorrowed()) {
			// Mark the book as not borrowed
			book.setBorrowed(false);
			client.getOwnedBooks().remove(book);
			bookLibrary.addBook(book);

			// Perform additional operations related to the book return, such as updating records in the library or client's borrowing history.
			// For example, you might want to update the client's borrowing history or remove the book from the list of borrowed books.

			// Return true to indicate the success of the operation
			return false;
		} else {
			// If the book is not borrowed by the client, or the book is not marked as borrowed, return false to indicate failure
			return borrowed;
		}
	}

	public void setBorrowed(boolean borrowed) {
		this.borrowed = borrowed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void markAsSold() {
		this.sold = true;
	}

	public void saveToDatabase(Connection connection) {
		if (isBookInDatabase(connection)) {
			System.out.println("Book with ID " + getId() + " already exists in the library. Skipping insertion.");
		} else {
			insertOrUpdateBook(connection);
		}
	}

	boolean isBookInDatabase(Connection connection) {
		String selectQuery = "SELECT id FROM books WHERE id = ?";
		try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
			selectStatement.setInt(1, getId());
			ResultSet resultSet = selectStatement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
	}

	private void insertOrUpdateBook(Connection connection) {
		String insertQuery = "INSERT INTO books (id, title, author, price, sold, borrowed) VALUES (?, ?, ?, ?, ?, ?) " +
				"ON DUPLICATE KEY UPDATE id=id";
		try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
			insertStatement.setInt(1, getId());
			insertStatement.setString(2, getTitle());
			insertStatement.setString(3, getAuthor());
			insertStatement.setDouble(4, getPrice());
			insertStatement.setBoolean(5, isSold());
			insertStatement.setBoolean(6, isBorrowed());
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	private void handleSQLException(SQLException e) {
		e.printStackTrace(); // Handle the exception appropriately, log it, or throw a custom exception.
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, author, price, sold, borrowed);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Book otherBook = (Book) obj;

		return id == otherBook.id &&
				Objects.equals(title, otherBook.title) &&
				Objects.equals(author, otherBook.author) &&
				Double.compare(otherBook.price, price) == 0 &&
				sold == otherBook.sold&& borrowed == otherBook.borrowed;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", price=" + price +
				", sold=" + sold +
				", borrowed=" + borrowed +
				'}';
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isSold() {
		return this.sold=true;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public boolean isBorrowed() {
		return borrowed;
	}


	public boolean borrowBook(BookLibrary bookLibrary, Client client) {
		if (!borrowed) {
			// If the book is not already borrowed, mark it as borrowed
			setBorrowed(true);

			// Remove the book from the library's available books and add it to the client's borrowed books
			if (bookLibrary.getAllBooks().remove(this)) {
				bookLibrary.getBorrowedBooks().add(this);
				client.addBorrowedBook(this);

				System.out.println("Book borrowed: " + getTitle());
				return true;
			} else {
				// If the book was not found in the library's available books, it cannot be borrowed
				System.out.println("Book not found in the library's available books.");
				setBorrowed(false);
				return false;
			}
		} else {
			// If the book is already borrowed, it cannot be borrowed again
			System.out.println("Book is already borrowed.");
			return false;
		}
	}

}

/*
Назви методів:

Перейменовано isBookInDatabase, щоб краще відобразити його призначення.
Перейменовано insertBookIntoDatabase на insertOrUpdateBook для ясності.
Послідовна обробка винятків:

Перенесено метод handleSQLException в окремий приватний метод для узгодженої обробки винятків.
Очищувач saveToDatabase:

Спрощено метод saveToDatabase за допомогою покращених методів для перевірки та вставлення/оновлення книги.
Послідовний порядок методів:

Методи переставлено для більш логічного порядку.
Коментарі:

Видалено непотрібні коментарі, щоб покращити читабельність коду. Хороший код має бути зрозумілим.
  */

