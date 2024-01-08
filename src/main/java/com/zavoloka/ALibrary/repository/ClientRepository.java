package com.zavoloka.ALibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zavoloka.ALibrary.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // You can add custom queries here if needed
}
