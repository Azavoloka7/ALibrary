
package com.zavoloka;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Manager {
	private int id;
	private int age;
	private String firstName;
	private String lastName;
	private List<Book> allBooks = new ArrayList<>();
	private int salesCount;
	private int sales;
	private int experience;

	// Default constructor
	public Manager() {
	}

	// Parameterized constructor
	public Manager(int id, int age, String firstName, String lastName, int sales, int experience) {
		this.id = id;
		this.age = age;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sales = sales;
		this.experience = experience;
	}

	// Getter and setter methods for each field
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	// Getter for allBooks
	public List<Book> getAllBooks() {
		return allBooks;
	}

	// Increment sales count
	public void incrementSalesCount() {
		this.salesCount++;
	}

	// Increment sales
	public void incrementSales() {
		this.sales++;
	}

	// Method to add book to the library
	public void addBook(BookStore library, Book book) {
		library.addBook(book);
		System.out.println("Book added: " + book.getTitle());
	}

	// Getter for salesCount
	public int getSalesCount() {
		return salesCount;
	}

	// Setter for salesCount
	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}

	// toString method for better string representation
	@Override
	public String toString() {
		return "Manager{" +
				"id=" + id +
				", age=" + age +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", sales=" + sales +
				", experience=" + experience +
				", salesCount=" + salesCount +
				", allBooks=" + allBooks +
				'}';
	}

	public void saveToDatabase(String jdbcUrl, String username, String password) {
		String insertManagerSql = "INSERT INTO managers (id, first_name, last_name, age) VALUES (?, ?, ?, ?)" +
				"ON DUPLICATE KEY UPDATE first_name=VALUES(first_name), last_name=VALUES(last_name), age=VALUES(age)";

		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			 PreparedStatement preparedStatement = connection.prepareStatement(insertManagerSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setInt(1, this.id);
			preparedStatement.setString(2, this.firstName);
			preparedStatement.setString(3, this.lastName);
			preparedStatement.setInt(4, this.age);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating manager failed, no rows affected.");
			}

			try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					this.id = generatedKeys.getInt(1);
				} else {
					// Existing record was updated, no new ID generated
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately, log it, or throw a custom exception.
		}
	}

}
