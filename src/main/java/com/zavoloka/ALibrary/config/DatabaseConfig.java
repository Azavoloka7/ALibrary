package com.zavoloka.ALibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // Replace these with your actual database credentials
        String url = "jdbc:mysql://localhost:3308/ALibrary";
        dataSource.setUrl(url);
        String username = "root";
        dataSource.setUsername(username);
        String password = "Z@v010ka";
        dataSource.setPassword(password);
        return dataSource;
    }
}
