package com.lambda.service.impl;

import com.lambda.dao.ClientDao;
import com.lambda.model.CustomClient;
import com.lambda.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("deprecation")
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    @Autowired
    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public List<CustomClient> findAll() {
        List<ClientDetails> rs = clientDao.listClientDetails();
        return rs
                .stream()
                .map(CustomClient::new)
                .collect(Collectors.toList());
    }

    @Override
    public CustomClient findById(String id) {
        return new CustomClient(clientDao.loadClientByClientId(id));
    }

    @Override
    public void create(CustomClient clientDetails) {
        clientDao.addClientDetails(clientDetails);
    }

    @Override
    public void update(CustomClient clientDetails) {
        clientDao.updateClientDetails(clientDetails);
    }

    @Override
    public void updateSecret(String clientId, String clientSecret) {
        clientDao.updateClientSecret(clientId, clientSecret);
    }

    @Override
    public void deleteById(String id) {
        clientDao.removeClientDetails(id);
    }
}
