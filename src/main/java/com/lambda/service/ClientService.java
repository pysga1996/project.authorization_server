package com.lambda.service;

import com.lambda.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Client findById(String id);
    void save(Client client);
    void deleteById(String id);
}
