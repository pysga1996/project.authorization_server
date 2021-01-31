package com.lambda.client.impl;

import com.lambda.client.NotificationClient;
import com.lambda.config.custom.HostResolver;
import com.lambda.constant.TokenStatus;
import com.lambda.constant.TokenType;
import com.lambda.dao.TokenDao;
import com.lambda.model.ws.EmailAuthenticationRequest;
import com.lambda.model.ws.ObjectFactory;
import com.lambda.model.ws.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.concurrent.Executor;

@Component("notificationClient")
public class NotificationClientImpl extends WebServiceGatewaySupport implements NotificationClient {

    private static final Logger logger = LogManager.getLogger(NotificationClientImpl.class);

    @Value("${webservices.url.notification}")
    private String webserviceUrl;

    private final ObjectFactory objectFactory;

    private final MessageSource messageSource;

    private final Executor asyncExecutor;

    private final HostResolver hostResolver;

    private final TokenDao tokenDao;

    @Autowired
    public NotificationClientImpl(ObjectFactory objectFactory, MessageSource messageSource,
                                  @Qualifier("asyncExecutor") Executor asyncExecutor,
                                  HostResolver hostResolver, TokenDao tokenDao) {
        this.objectFactory = objectFactory;
        this.messageSource = messageSource;
        this.asyncExecutor = asyncExecutor;
        this.hostResolver = hostResolver;
        this.tokenDao = tokenDao;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendRegistrationEmail(String username, String email, String clientRedirectUrl, Locale locale) {
        this.asyncExecutor.execute(() -> {
            String subject = this.messageSource.getMessage("registration.email.title", new Object[]{}, locale);
            String text = this.messageSource.getMessage("registration.email.text", new Object[]{}, locale);
            String redirectUrl = null;
            try {
                redirectUrl = this.hostResolver.resolveHost("/api/registration-confirm");
            } catch (UnknownHostException e) {
                logger.error("Error while resolving host");
            }
            EmailAuthenticationRequest request = this
                    .createEmailAuthenticationRequest(this.objectFactory, email, text, redirectUrl, subject);
            logger.info("Sending registration email to: {}", email);
            JAXBElement<Response> response = (JAXBElement<Response>) getWebServiceTemplate()
                    .marshalSendAndReceive(this.webserviceUrl, request);
            int code = response.getValue().getCode();
            if (code == 0) {
                String token = response.getValue().getDescription();
                this.tokenDao.insertToken(token, username, TokenType.REGISTRATION, TokenStatus.ACTIVE, clientRedirectUrl);
            } else {
                logger.error("Error while sending registration email to {}: {}",
                        email, response.getValue().getDescription());
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendPasswordResetEmail(String username, String email, String clientRedirectUrl, Locale locale) {
        this.asyncExecutor.execute(() -> {
            String subject = messageSource.getMessage("reset-password.email.title", new Object[]{}, locale);
            String text = messageSource.getMessage("reset-password.email.text", new Object[]{}, locale);
            String redirectUrl = null;
            try {
                redirectUrl = this.hostResolver.resolveHost("/api/reset-password");
            } catch (UnknownHostException e) {
                logger.error("Error while resolving host");
            }
            EmailAuthenticationRequest request = this
                    .createEmailAuthenticationRequest(this.objectFactory, email, text, redirectUrl, subject);
            logger.info("Sending reset password email to: {}", email);
            JAXBElement<Response> response = (JAXBElement<Response>) getWebServiceTemplate()
                    .marshalSendAndReceive(this.webserviceUrl, request);
            int code = response.getValue().getCode();
            if (code == 0) {
                String token = response.getValue().getDescription();
                this.tokenDao.insertToken(token, username, TokenType.RESET_PASSWORD, TokenStatus.INACTIVE, clientRedirectUrl);
            } else {
                logger.error("Error while sending reset password email to {}: {}",
                        email, response.getValue().getDescription());
            }
        });
    }
}
