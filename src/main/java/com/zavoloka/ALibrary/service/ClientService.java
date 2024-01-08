package com.zavoloka.ALibrary.service;

import java.util.List;
import java.util.Optional;

import com.zavoloka.ALibrary.model.Client;

public interface ClientService {

    List<Client> getAllClients();

    Optional<Client> getClientById(Long id);

    Client saveClient(Client client);

    void deleteClient(Long id);
}
