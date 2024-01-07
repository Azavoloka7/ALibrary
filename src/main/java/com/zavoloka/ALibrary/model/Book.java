package com.zavoloka.ALibrary.model;

import com.zavoloka.ALibrary.BookStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private double price;
    private int sold;
    private boolean borrowed;
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    public Object getId() {
        return id;
    }

    public Object getTitle() {
        return title;
    }

    public Object getAuthor() {
        return author;
    }

    public Object getPrice() {
        return price;
    }

    public Object getSold() {
        return sold;
    }

    public Object getBorrowed() {
        return borrowed;
    }

    public void setTitle(Object title) {
        this.title = (String) title;
    }

    public void setAuthor(Object author) {
        this.author = (String) author;
    }

    public void setPrice(Object price) {
        this.price = (double) price;
    }

    public void setSold(Object sold) {
        this.sold = (int) sold;
    }

    public void setBorrowed(Object borrowed) {
        this.borrowed = (boolean) borrowed;
    }


}
