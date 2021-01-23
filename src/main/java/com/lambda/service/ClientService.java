package com.lambda.service;

import com.lambda.model.CustomClient;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.List;

@SuppressWarnings("deprecation")
public interface ClientService {

    List<CustomClient> findAll();

    CustomClient findById(String id);

    void create(CustomClient clientDetails);

    void update(CustomClient clientDetails);

    void updateSecret(String clientId, String clientSecret);

    void deleteById(String id);
}
