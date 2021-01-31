package com.lambda.model.dto;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@SuppressWarnings("deprecation")
public class ClientDTO extends BaseClientDetails implements ClientDetails {

    public ClientDTO(ClientDetails prototype) {
        super(prototype);
    }
}
