package com.lambda.service.impl;

import com.lambda.dao.ClientDao;
import com.lambda.model.Client;
import com.lambda.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private ClientDao clientDao;

    @Autowired
    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public List<Client> findAll() {
        return clientDao.getAllClients();
    }

    @Override
    public Client findById(String id) {
        return clientDao.getClientById(id);
    }

    @Override
    public void save(Client client) {
        clientDao.saveOrUpdateClient(client);
    }

    @Override
    public void deleteById(String id) {
        clientDao.deleteClientById(id);
    }
}
