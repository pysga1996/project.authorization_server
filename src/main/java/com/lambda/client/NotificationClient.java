package com.lambda.client;

import com.lambda.model.ws.EmailAuthenticationRequest;
import com.lambda.model.ws.ObjectFactory;

import java.util.Locale;

public interface NotificationClient {

    void sendRegistrationEmail(String username, String email, String clientRedirectUrl, Locale locale);

    void sendPasswordResetEmail(String username, String email, String clientRedirectUrl, Locale locale);

    default EmailAuthenticationRequest createEmailAuthenticationRequest(ObjectFactory objectFactory,
                                                                        String email, String text,
                                                                        String redirectUrl, String subject) {
        EmailAuthenticationRequest request = objectFactory.createEmailAuthenticationRequest();
        request.setDescription(text);
        request.setEmail(email);
        request.setRedirectUrl(redirectUrl);
        request.setSubject(subject);
        return request;
    }
}
