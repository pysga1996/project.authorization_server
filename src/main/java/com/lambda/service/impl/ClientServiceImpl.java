package com.lambda.service.impl;

import com.lambda.dao.ClientDao;
import com.lambda.model.dto.ClientDTO;
import com.lambda.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.*;
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
        List<ClientDetails> rs = this.clientDao.listClientDetails();
        return rs
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO findById(String id) {
        return new ClientDTO(this.clientDao.loadClientByClientId(id));
    }

    @Override
    @Transactional
    public void create(ClientDTO clientDetails) {
        this.clientDao.addClientDetails(clientDetails);
    }

    @Override
    @Transactional
    public void update(ClientDTO clientDetails) {
        this.clientDao.updateClientDetails(clientDetails);
    }

    @Override
    @Transactional
    public void updateSecret(String clientId, String clientSecret) {
        this.clientDao.updateClientSecret(clientId, clientSecret);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.clientDao.removeClientDetails(id);
    }

    @Override
    public Cookie createCookie(ClientDTO clientDTO) {
        String clientId = clientDTO.getClientId();
        Set<String> scope = clientDTO.getScope() == null ? new HashSet<>() : clientDTO.getScope();
        Set<String> autoApproveScopes = clientDTO.getAutoApproveScopes() == null ?
                new HashSet<>() : clientDTO.getAutoApproveScopes();
        Set<String> authorities = clientDTO.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        Set<String> registeredRedirectUri = clientDTO.getRegisteredRedirectUri() == null ?
                new HashSet<>() : clientDTO.getRegisteredRedirectUri();
        String accessTokenValiditySeconds = clientDTO.getAccessTokenValiditySeconds() == null ?
                String.valueOf(60) : String.valueOf(clientDTO.getAccessTokenValiditySeconds());
        String refreshTokenValiditySeconds = clientDTO.getRefreshTokenValiditySeconds() == null ?
                String.valueOf(60 * 60) : String.valueOf(clientDTO.getRefreshTokenValiditySeconds());
        List<String> attrList = Arrays.asList(clientId,
                String.join(",", scope),
                String.join(",", autoApproveScopes),
                String.join(",", authorities),
                String.join(",", registeredRedirectUri),
                accessTokenValiditySeconds,
                refreshTokenValiditySeconds);
        String cookieStr = String.join("|", attrList);
        return new Cookie("newClient", cookieStr);
    }

    @Override
    public void patchCookieToForm(String cookie, ClientDTO clientDTO) {
        if (cookie == null) return;
        try {
            String[] values = cookie.split("\\|");
            String clientId = values[0];
            Collection<String> scope = values[1].trim().equals("") ?
                    new HashSet<>() : Arrays.asList(values[1].split(","));
            Collection<String> autoApproveScopes = values[2].trim().equals("") ?
                    new HashSet<>() : Arrays.asList(values[2].split(","));
            Collection<? extends GrantedAuthority> authorities = values[3].trim().equals("") ?
                    new HashSet<>() : Arrays.stream(values[3].split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            Set<String> registeredRedirectUri = values[4].trim().equals("") ?
                    new HashSet<>() : Arrays.stream(values[4].split(","))
                    .collect(Collectors.toSet());
            clientDTO.setClientId(clientId);
            clientDTO.setScope(scope);
            clientDTO.setAutoApproveScopes(autoApproveScopes);
            clientDTO.setAuthorities(authorities);
            clientDTO.setRegisteredRedirectUri(registeredRedirectUri);
            clientDTO.setAccessTokenValiditySeconds(Integer.valueOf(values[5]));
            clientDTO.setRefreshTokenValiditySeconds(Integer.valueOf(values[6]));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
