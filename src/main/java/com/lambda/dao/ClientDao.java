package com.lambda.dao;

import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.List;

@SuppressWarnings("deprecation")
public interface ClientDao extends ClientDetailsService {

    ClientDetails loadClientByClientId(String clientId) throws InvalidClientException;

    void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException;

    void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException;

    void updateClientSecret(String clientId, String secret) throws NoSuchClientException;

    void removeClientDetails(String clientId) throws NoSuchClientException;

    List<ClientDetails> listClientDetails();
}
