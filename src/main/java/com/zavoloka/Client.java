package com.zavoloka;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Client {
	private int id;
	private int age;
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private int borrowedBooks;
	private double balance;
	private BookStore bookStore;
	private List<Book> ownedBooks = new ArrayList<>();

	// Default constructor
	public Client() {
		this.bookStore = new BookStore(); // Initialize the BookStore variable
	}

	// Parameterized constructor
	public Client(int id, double balance, int age, String firstName, String lastName, String address, String phoneNumber) {
		this.id = id;
		this.balance = balance;
		this.age = age;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	// Getter and setter methods for each field
	// ... (no changes here)
	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}



	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	// Getter for ownedBooks
	public Collection<String> getOwnedBooks() {
		Collection<String> ownedBooksCollection = new ArrayList<>();
		for (Book book : ownedBooks) {
			ownedBooksCollection.add(book.toString());
		}
		return ownedBooksCollection;
	}
	public void saveToDatabase(String jdbcUrl, String username, String password) {
		String selectClientSql = "SELECT id FROM clients WHERE phone_number = ?";
		String insertClientSql = "INSERT INTO clients (balance, first_name, last_name, address, phone_number) " +
				"VALUES (?, ?, ?, ?, ?) " +
				"ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id), " +
				"balance=VALUES(balance), first_name=VALUES(first_name), " +
				"last_name=VALUES(last_name), address=VALUES(address), phone_number=VALUES(phone_number)";

		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			 PreparedStatement selectStatement = connection.prepareStatement(selectClientSql);
			 PreparedStatement preparedStatement = connection.prepareStatement(insertClientSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			// Check if a client with the same phone number already exists
			selectStatement.setString(1, phoneNumber);
			ResultSet resultSet = selectStatement.executeQuery();

			if (!resultSet.next()) {
				// No client with the same phone number found, proceed with insertion
				preparedStatement.setDouble(1, balance);
				preparedStatement.setString(2, firstName);
				preparedStatement.setString(3, lastName);
				preparedStatement.setString(4, address);
				preparedStatement.setString(5, phoneNumber);

				int affectedRows = preparedStatement.executeUpdate();

				if (affectedRows == 0) {
					throw new SQLException("Creating client failed, no rows affected.");
				}

				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						id = generatedKeys.getInt(1);
					} else {
						// Existing record was updated, no new ID generated
					}
				}
			} else {
				// Client with the same phone number already exists, skip insertion
				System.out.println("Client with phone number " + phoneNumber + " already exists in the database. Skipping insertion.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately, log it, or throw a custom exception.
		}
	}




	private void setId(int id) {
		this.id = id;
	}

	// Increment borrowed books count
	public void addBorrowedBook(Book bookToBorrow) {
		System.out.println("Book added to borrowed books: " + bookToBorrow.getTitle());
		bookToBorrow.setBorrowed(true);
	}

	// Method to process a purchase
	private void processPurchase(Book book) {
		// Assuming you want to add the purchased book to the client's owned books
		ownedBooks.add(book);
		book.isSold();
		// You might want to update other purchase-related logic here
	}

	// Method to update inventory after borrowing or returning a book
	private void updateInventory(Book book) {
		// Assuming you want to remove the borrowed or returned book from the client's owned books
		ownedBooks.remove(book);
		book.setBorrowed(false);
		// You might want to update other inventory-related logic here
	}

	// Method to buy a book
	public void buyBook(Manager manager, Book bookToSell) {
		double bookPrice = bookToSell.getPrice();

		if (hasSufficientBalance(bookPrice)) {
			manager.incrementSales();
			this.balance -= bookToSell.getPrice();
			processPurchase(bookToSell);
			updateClientBalance(bookPrice);
			System.out.println("Book purchased: " + bookToSell.getTitle());
		} else {
			System.out.println("Insufficient balance to buy the book: " + bookToSell.getTitle());
		}
	}

	// Method to make a payment
	public void makePayment(double price) {
		if (isValidPaymentAmount(price)) {
			updateClientBalance(-price);
			System.out.println("Payment successful. Remaining balance: " + getBalance());
		} else {
			System.out.println("Insufficient funds. Payment failed.");
		}
	}

	// Getter for balance
	public double getBalance() {
		return balance;
	}

	// Method to check if the payment amount is valid
	private boolean isValidPaymentAmount(double price) {
		return price > 0;
	}

	// Method to check if the client has sufficient balance
	private boolean hasSufficientBalance(double amount) {
		return amount > 0 && balance >= amount;
	}

	// Method to update client's balance
	private void updateClientBalance(double amount) {
		// Assuming you want to update the client's balance by a given amount
		balance += amount;
		System.out.println("Balance updated. New balance: " + balance);
	}

	// Setter for balance
	private void setBalance(double balance) {
		this.balance = balance;
	}

	// toString method for better string representation
	@Override
	public String toString() {
		return "Client{" +
				"id=" + id +
				", age=" + age +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", address='" + address + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}

	// hashCode and equals methods for proper comparison and hashing
	@Override
	public int hashCode() {
		return Objects.hash(id, age, firstName, lastName, address, phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Client client = (Client) obj;
		return id == client.id &&
				age == client.age &&
				Objects.equals(firstName, client.firstName) &&
				Objects.equals(lastName, client.lastName) &&
				Objects.equals(address, client.address) &&
				Objects.equals(phoneNumber, client.phoneNumber);
	}

	// Method to borrow a book from the library
	public void borrowBook(BookLibrary library, Book bookToBorrow) {
		boolean success = library.borrowBook(bookToBorrow, this);

		if (success) {
			System.out.println("Book borrowed: " + bookToBorrow.getTitle());
			addBorrowedBook(bookToBorrow);
			updateInventory(bookToBorrow);
			bookToBorrow.getBorrowed();
		} else {
			System.out.println("Book not available for borrowing: " + bookToBorrow.getTitle());
		}
	}

	// Method to return a book to the library
	public void returnBook(BookLibrary library, Book bookToReturn) {
		library.addBook(bookToReturn);
		System.out.println("Book returned: " + bookToReturn.getTitle());
		updateInventory(bookToReturn);
	}


}

/*
Основні зміни:

Метод processPurchase: Доданий заготовочний метод для обробки покупки книги. Необхідно реалізувати його функціонал відповідно до бізнес-логіки вашого додатку.

Метод updateInventory: Доданий заготовочний метод для оновлення інвентарю після видачі або повернення книги. 

Методи hasSufficientBalance і updateClientBalance: Додані приватні методи для перевірки наявності достатнього балансу та оновлення балансу клієнта. Це допомагає забезпечити чистий та читабельний код.

Метод isValidPaymentAmount: Доданий приватний метод для перевірки коректності суми платежу. Це допомагає забезпечити чистий та читабельний код.

Ці зміни спрощують читання та розуміння коду, а також готують його до додавання реальної бізнес-логіки в майбутньому.

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.zavoloka.BookStore.manager;

public class Client {
	private int id;
	private int age;
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private int borrowedBooks;
	private double balance;
	private BookStore bookStore;
	private List<Book> ownedBooks = new ArrayList<>();
	// Default constructor

	public Client() {
		this.bookStore = new BookStore(); // Initialize the BookStore variable
	}

	private void processPurchase(Book book) {
	}

	// Parameterized constructor
	public Client(int id, double balance, int age, String firstName, String lastName, String address, String phoneNumber) {
		this.id = id;
		this.balance = balance;
		this.age = age;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	// Getter and setter methods for each field
	public Collection<String> getOwnedBooks() {
		Collection<String> ownedBooksCollection = new ArrayList<>();
		for (Book book : ownedBooks) {
			ownedBooksCollection.add(book.toString());
		}
		return ownedBooksCollection;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	// toString method for better string representation

	@Override
	public String toString() {
		return "Client{" +
				"id=" + id +
				", age=" + age +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", address='" + address + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}

	// hashCode and equals methods for proper comparison and hashing
	@Override
	public int hashCode() {
		return Objects.hash(id, age, firstName, lastName, address, phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Client client = (Client) obj;
		return id == client.id &&
				age == client.age &&
				Objects.equals(firstName, client.firstName) &&
				Objects.equals(lastName, client.lastName) &&
				Objects.equals(address, client.address) &&
				Objects.equals(phoneNumber, client.phoneNumber);
	}

	public void borrowBook(BookLibrary library, Book bookToBorrow) {
		boolean success = library.borrowBook(bookToBorrow, this);

		if (success) {
			System.out.println("Book borrowed: " + bookToBorrow.getTitle());
		} else {
			System.out.println("Book not available for borrowing: " + bookToBorrow.getTitle());
		}
	}

	public void returnBook(BookLibrary library, Book bookToReturn) {
		library.addBook(bookToReturn);

		System.out.println("Book returned: " + bookToReturn.getTitle());
	}

	public void addBorrowedBook(Book bookToBorrow) {

		System.out.println("Book added to borrowed books: " + bookToBorrow.getTitle());

	}
	private void updateInventory(Book book) {


	}
	public void buyBook(Book bookToSell) {
		// Assuming the Book class has a getPrice method to get the book price
		double bookPrice = bookToSell.getPrice();

		// Check if the client has enough balance to buy the book
		if (getBalance() >= bookPrice) {
			// Process the purchase
			processPurchase(bookToSell);

			// Update client's balance
			setBalance(getBalance() - bookPrice);

			// Display a success message
			System.out.println("Book purchased: " + bookToSell.getTitle());
		} else {
			// Display an insufficient balance message
			System.out.println("Insufficient balance to buy the book: " + bookToSell.getTitle());
		}
	}




	public void makePayment(double price) {
		// Assuming there is a balance field to keep track of the client's funds
		if (price > 0 && getBalance() >= price) {
			setBalance(getBalance() - price);
			System.out.println("Payment successful. Remaining balance: " + getBalance());
		} else {
			System.out.println("Insufficient funds. Payment failed.");
		}
	}


}
*/