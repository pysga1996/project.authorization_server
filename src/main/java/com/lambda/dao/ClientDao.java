package com.lambda.dao;

import com.lambda.model.Client;

import java.util.List;

public interface ClientDao {
    List<Client> getAllClients();
    Client getClientById(String id);
    void saveOrUpdateClient(Client client);
    void deleteClientById(String id);
}
