package com.zavoloka.ALibrary.repository;

import com.zavoloka.ALibrary.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // You can add custom queries here if needed
}
