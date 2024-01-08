package com.zavoloka.ALibrary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zavoloka.ALibrary.model.Book;
import com.zavoloka.ALibrary.repository.BookRepository;

@Service
public class BookService {

    private  BookRepository bookRepository = null;

    @Autowired
    public BookService() {
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
