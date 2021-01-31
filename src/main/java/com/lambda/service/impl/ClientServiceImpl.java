package com.lambda.service.impl;

import com.lambda.dao.ClientDao;
import com.lambda.model.dto.ClientDTO;
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
    public List<ClientDTO> findAll() {
        List<ClientDetails> rs = clientDao.listClientDetails();
        return rs
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO findById(String id) {
        return new ClientDTO(clientDao.loadClientByClientId(id));
    }

    @Override
    public void create(ClientDTO clientDetails) {
        clientDao.addClientDetails(clientDetails);
    }

    @Override
    public void update(ClientDTO clientDetails) {
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
