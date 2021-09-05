package com.lambda.model.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("deprecation")
public class ClientDTO extends BaseClientDetails implements ClientDetails {

    private Set<String> authorities;

    public ClientDTO(ClientDetails prototype) {
        super(prototype);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return new HashSet<>(super.getAuthorities());
    }

    public void setAuthorities(Set<String> authorities) {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        this.authorities = authorities;
        super.setAuthorities(this.authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet()));
    }

    @Override
    public void setAutoApproveScopes(Collection<String> autoApproveScopes) {
        if (autoApproveScopes == null) {
            autoApproveScopes = new HashSet<>();
        }
        super.setAutoApproveScopes(autoApproveScopes);
    }
}
