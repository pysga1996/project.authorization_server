package com.lambda.service;

import com.lambda.model.dto.ClientDTO;

import java.util.List;

@SuppressWarnings("deprecation")
public interface ClientService {

    List<ClientDTO> findAll();

    ClientDTO findById(String id);

    void create(ClientDTO clientDetails);

    void update(ClientDTO clientDetails);

    void updateSecret(String clientId, String clientSecret);

    void deleteById(String id);
}
