package com.lambda.model;

import com.lambda.dto.soap.user.AuthorityDTO;
import com.lambda.dto.soap.client.ClientDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@SuppressWarnings("deprecation")
public class CustomClient extends BaseClientDetails {

    public CustomClient(ClientDetails prototype) {
        super(prototype);
    }

    public CustomClient(ClientDTO clientDTO) {
        CustomClient customClient = new CustomClient();
        customClient.setClientId(clientDTO.getClientId());
        customClient.setClientSecret(clientDTO.getClientSecret());
        customClient.setResourceIds(clientDTO.getResourceIds());
        customClient.setAdditionalInformation(clientDTO.getAdditionalInformation());
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (AuthorityDTO authorityDTO : clientDTO.getAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authorityDTO.getAuthority());
            authorities.add(grantedAuthority);
        }
        customClient.setAuthorities(authorities);
        customClient.setAccessTokenValiditySeconds(clientDTO.getAccessTokenValiditySeconds());
        customClient.setRefreshTokenValiditySeconds(clientDTO.getRefreshTokenValiditySeconds());
        customClient.setScope(clientDTO.getScope());
        customClient.setRegisteredRedirectUri(new HashSet<>(clientDTO.getRegisteredRedirectUris()));
        customClient.setAuthorizedGrantTypes(clientDTO.getAuthorizedGrantTypes());
        customClient.setAutoApproveScopes(clientDTO.getAutoApproveScopes());
    }

    public ClientDTO toDTO() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientId(this.getClientId());
        clientDTO.setClientSecret(this.getClientSecret());
        clientDTO.setAuthorities(this.getAuthorities()
                .stream()
                .map(e -> {
                    AuthorityDTO authorityDTO = new AuthorityDTO();
                    authorityDTO.setAuthority(e.getAuthority());
                    return authorityDTO;
                }).collect(Collectors.toList()));

        clientDTO.setAccessTokenValiditySeconds(this.getAccessTokenValiditySeconds());
        clientDTO.setRefreshTokenValiditySeconds(this.getRefreshTokenValiditySeconds());
        clientDTO.setAdditionalInformation(this.getAdditionalInformation());
        if (this.getResourceIds() != null) {
            clientDTO.getResourceIds().addAll(this.getResourceIds());
        }
        if (this.getScope() != null) {
            clientDTO.getScope().addAll(this.getScope());
        }
        if (this.getAuthorizedGrantTypes() != null) {
            clientDTO.getAuthorizedGrantTypes().addAll(this.getAuthorizedGrantTypes());
        }
        if (this.getAutoApproveScopes() != null) {
            clientDTO.getAutoApproveScopes().addAll(this.getAutoApproveScopes());
        }
        return clientDTO;
    }
}
