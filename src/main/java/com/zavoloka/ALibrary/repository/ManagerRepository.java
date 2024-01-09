package com.zavoloka.ALibrary.repository;

import com.zavoloka.ALibrary.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    // You can add custom queries here if needed
}
