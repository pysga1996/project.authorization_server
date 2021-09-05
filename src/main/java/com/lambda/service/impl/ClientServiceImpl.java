package com.lambda.service.impl;

import com.lambda.dao.ClientDao;
import com.lambda.model.dto.ClientDTO;
import com.lambda.service.ClientService;
import com.lambda.util.CookieSecureCreator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
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
    public Cookie[] createCookie(ClientDTO clientDTO) {
        String clientId = clientDTO.getClientId();
        Cookie clientIdCookie = CookieSecureCreator.create("clientId", clientId);
        Set<String> scope = clientDTO.getScope() == null ? new HashSet<>() : clientDTO.getScope();
        Cookie scopeCookie = CookieSecureCreator.create("scope", String.join("|", scope));
        Set<String> autoApproveScopes = clientDTO.getAutoApproveScopes() == null ?
            new HashSet<>() : clientDTO.getAutoApproveScopes();
        Cookie autoApproveScopesCookie = CookieSecureCreator
            .create("autoApproveScopes", String.join("|", autoApproveScopes));
        Set<String> authorities = clientDTO.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        Cookie authoritiesCookie = CookieSecureCreator
            .create("authorities", String.join("|", authorities));
        Set<String> registeredRedirectUri = clientDTO.getRegisteredRedirectUri() == null ?
            new HashSet<>() : clientDTO.getRegisteredRedirectUri();
        Cookie registeredRedirectUriCookie = CookieSecureCreator
            .create("registeredRedirectUri", String.join("|", registeredRedirectUri));
        String accessTokenValiditySeconds = clientDTO.getAccessTokenValiditySeconds() == null ?
            String.valueOf(60) : String.valueOf(clientDTO.getAccessTokenValiditySeconds());
        Cookie accessTokenValiditySecondsCookie = CookieSecureCreator
            .create("accessTokenValiditySeconds", accessTokenValiditySeconds);
        String refreshTokenValiditySeconds = clientDTO.getRefreshTokenValiditySeconds() == null ?
            String.valueOf(60 * 60) : String.valueOf(clientDTO.getRefreshTokenValiditySeconds());
        Cookie refreshTokenValiditySecondsCookie = CookieSecureCreator
            .create("refreshTokenValiditySeconds", refreshTokenValiditySeconds);
        return new Cookie[]{clientIdCookie, scopeCookie, autoApproveScopesCookie, authoritiesCookie,
            registeredRedirectUriCookie, accessTokenValiditySecondsCookie,
            refreshTokenValiditySecondsCookie};
    }

    @Override
    public void patchCookiesToForm(String clientId, String scopeCookie, String autoApproveScopes,
        String authorities, String registeredRedirectUri,
        String accessTokenValiditySeconds, String refreshTokenValiditySeconds,
        ClientDTO clientDTO) {
        try {
            if (clientId != null) {
                clientDTO.setClientId(clientId);
            }
            if (scopeCookie != null && !scopeCookie.trim().equals("")) {
                clientDTO.setScope(Arrays.asList(scopeCookie.trim().split("\\|")));
            }
            if (autoApproveScopes != null && !autoApproveScopes.trim().equals("")) {
                clientDTO
                    .setAutoApproveScopes(Arrays.asList(autoApproveScopes.trim().split("\\|")));
            }
            if (authorities != null && !authorities.trim().equals("")) {
                clientDTO
                    .setAuthorities(new HashSet<>(Arrays.asList(authorities.trim().split("\\|"))));
            }
            if (registeredRedirectUri != null && !registeredRedirectUri.trim().equals("")) {
                clientDTO.setRegisteredRedirectUri(new HashSet<>(Arrays
                    .asList(registeredRedirectUri.split("\\|"))));
            }
            if (accessTokenValiditySeconds != null) {
                clientDTO
                    .setAccessTokenValiditySeconds(Integer.valueOf(accessTokenValiditySeconds));
            }
            if (refreshTokenValiditySeconds != null) {
                clientDTO
                    .setRefreshTokenValiditySeconds(Integer.valueOf(refreshTokenValiditySeconds));
            }
        } catch (Exception ex) {
            log.error("Error while patch cookie to form", ex);
        }
    }
}
