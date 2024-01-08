package com.zavoloka.ALibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zavoloka.ALibrary.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> displayAll();
    // Add custom query method for downloading books, if needed
}
