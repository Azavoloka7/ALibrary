package com.zavoloka.ALibrary.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // Other fields, constructors, getters, and setters

    // Constructors

    public Client() {
        // Default constructor needed for JPA
    }

    public Client(String name) {
        this.name = name;
    }

    // Getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString, equals, hashCode, etc. methods if needed
}

