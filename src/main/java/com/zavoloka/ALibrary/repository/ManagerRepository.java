package com.zavoloka.ALibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zavoloka.ALibrary.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    // You can add custom queries here if needed
}
