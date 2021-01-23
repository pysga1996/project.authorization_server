package com.lambda.endpoint;

import com.lambda.constant.WebServiceConstant;
import com.lambda.dto.soap.client.*;
import com.lambda.model.CustomClient;
import com.lambda.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.addressing.server.annotation.Action;

import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class ClientEndpoint {

    private final ClientService clientService;

    @Autowired
    public ClientEndpoint(ClientService clientService) {
        this.clientService = clientService;
    }

    //    @Action("http://www.lambda.authorization/clients/list")
    @PayloadRoot(namespace = WebServiceConstant.CLIENTS_NAMESPACE, localPart = "GetClientRequest")
    @ResponsePayload
    public GetClientResponse getClientList(@RequestPayload GetClientRequest request) {
        GetClientResponse response = new GetClientResponse();
        List<ClientDTO> clientDTOList = clientService.findAll()
                .stream()
                .map(CustomClient::toDTO)
                .collect(Collectors.toList());
        response.setClientList(clientDTOList);
        return response;
    }

    //    @Action("http://www.lambda.authorization/clients/create")
    @PayloadRoot(namespace = WebServiceConstant.CLIENTS_NAMESPACE, localPart = "CreateClient")
    @ResponsePayload
    public CreateClientResponse createClient(@RequestPayload CreateClientRequest createClientRequest) {
        clientService.create(new CustomClient(createClientRequest.getClient()));
        return new CreateClientResponse();
    }
}
