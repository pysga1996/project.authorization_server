package com.lambda.client.impl;

import com.lambda.client.NotificationClient;
import com.lambda.constant.TokenStatus;
import com.lambda.constant.TokenType;
import com.lambda.dao.TokenDao;
import com.lambda.model.ws.EmailAuthenticationRequest;
import com.lambda.model.ws.ObjectFactory;
import com.lambda.model.ws.Response;
import java.util.Locale;
import java.util.concurrent.Executor;
import javax.xml.bind.JAXBElement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Log4j2
@Component("notificationClient")
public class NotificationClientImpl extends WebServiceGatewaySupport implements NotificationClient {

    private final ObjectFactory objectFactory;
    private final MessageSource messageSource;
    private final Executor asyncExecutor;
    private final TokenDao tokenDao;
    @Value("${webservices.url.notification}")
    private String webserviceUrl;

    @Autowired
    public NotificationClientImpl(ObjectFactory objectFactory, MessageSource messageSource,
        @Qualifier("asyncExecutor") Executor asyncExecutor, TokenDao tokenDao) {
        this.objectFactory = objectFactory;
        this.messageSource = messageSource;
        this.asyncExecutor = asyncExecutor;
        this.tokenDao = tokenDao;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendRegistrationEmail(String username, String email, String redirectUrl,
        Locale locale) {
        this.asyncExecutor.execute(() -> {
            String subject = this.messageSource
                .getMessage("registration.email.title", new Object[]{}, locale);
            String text = this.messageSource
                .getMessage("registration.email.text", new Object[]{}, locale);
            EmailAuthenticationRequest request = this
                .createEmailAuthenticationRequest(this.objectFactory, email, text, redirectUrl,
                    subject);
            log.info("Sending registration email to: {}", email);
            JAXBElement<Response> response = (JAXBElement<Response>) getWebServiceTemplate()
                .marshalSendAndReceive(this.webserviceUrl, request);
            int code = response.getValue().getCode();
            if (code == 0) {
                String token = response.getValue().getDescription();
                this.tokenDao
                    .insertToken(token, username, TokenType.REGISTRATION, TokenStatus.ACTIVE,
                        redirectUrl);
            } else {
                log.error("Error while sending registration email to {}: {}",
                    email, response.getValue().getDescription());
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendPasswordResetEmail(String username, String email, String redirectUrl,
        Locale locale) {
        this.asyncExecutor.execute(() -> {
            String subject = messageSource
                .getMessage("reset-password.email.title", new Object[]{}, locale);
            String text = messageSource
                .getMessage("reset-password.email.text", new Object[]{}, locale);
            EmailAuthenticationRequest request = this
                .createEmailAuthenticationRequest(this.objectFactory, email, text, redirectUrl,
                    subject);
            log.info("Sending reset password email to: {}", email);
            JAXBElement<Response> response = (JAXBElement<Response>) getWebServiceTemplate()
                .marshalSendAndReceive(this.webserviceUrl, request);
            int code = response.getValue().getCode();
            if (code == 0) {
                String token = response.getValue().getDescription();
                this.tokenDao
                    .insertToken(token, username, TokenType.RESET_PASSWORD, TokenStatus.ACTIVE,
                        redirectUrl);
            } else {
                log.error("Error while sending reset password email to {}: {}",
                    email, response.getValue().getDescription());
            }
        });
    }
}
