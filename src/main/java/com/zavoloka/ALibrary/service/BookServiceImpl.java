package com.zavoloka.ALibrary.service;

import com.zavoloka.ALibrary.model.Book;
import com.zavoloka.ALibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl extends BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return Optional.ofNullable(bookRepository.findById(id).orElse(null));
    }


    public Book addBook(Book book) {
        return null;
    }


    public Book updateBook(Long id, Book updatedBook) {
        return null;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
