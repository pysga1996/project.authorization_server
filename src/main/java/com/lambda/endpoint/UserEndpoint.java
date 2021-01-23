package com.lambda.endpoint;

import com.lambda.constant.WebServiceConstant;
import com.lambda.dto.soap.user.ResponseType;
import com.lambda.dto.soap.user.UserDTO;
import com.lambda.dto.soap.user.UserRequest;
import com.lambda.dto.soap.user.UserResponse;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class UserEndpoint {

    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PayloadRoot(namespace = WebServiceConstant.USERS_NAMESPACE, localPart = "UserRequest")
    @ResponsePayload
    public UserResponse getCurrentUser(@RequestPayload UserRequest request) {
        UserResponse response = new UserResponse();
        UserDTO userDTO = userService.getCurrentUserInfo();
        response.setType(ResponseType.SINGLE);
        response.setUser(userDTO);
        return response;
    }
}
