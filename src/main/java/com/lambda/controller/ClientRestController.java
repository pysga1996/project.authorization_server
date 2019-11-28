package com.lambda.controller;

import com.lambda.model.Client;
import com.lambda.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientRestController {
    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/list")
    public ModelAndView clientList() {
        try {
            List<Client> clientList = clientService.findAll();
            return new ModelAndView("client/client-list", "clientList", clientList) ;
        } catch (Exception e) {
            return new ModelAndView("server-error", "message", e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(WebRequest webRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return new ResponseEntity<>(currentPrincipalName, HttpStatus.OK);
    }
}
