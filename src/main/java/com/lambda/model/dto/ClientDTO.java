package com.lambda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("deprecation")
public class ClientDTO extends BaseClientDetails implements ClientDetails {

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return new HashSet<>(super.getAuthorities());
    }

    @Override
    public void setAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        super.setAuthorities(authorities);
    }

    @Override
    public void setAutoApproveScopes(Collection<String> autoApproveScopes) {
        if (autoApproveScopes == null) {
            autoApproveScopes = new HashSet<>();
        }
        super.setAutoApproveScopes(autoApproveScopes);
    }

    public ClientDTO(ClientDetails prototype) {
        super(prototype);
    }
}
