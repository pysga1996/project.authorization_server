package com.lambda.model.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.delta.model.ws package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation for XML content. The Java representation of
 * XML content can consist of schema derived interfaces and classes representing the binding of
 * schema type definitions, element declarations and model groups.  Factory methods for each of
 * these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendNotificationResponse_QNAME = new QName(
        "http://www.delta.notification", "sendNotificationResponse");
    private final static QName _EmailAuthenticationResponse_QNAME = new QName(
        "http://www.delta.notification", "emailAuthenticationResponse");
    private final static QName _SendSMSResponse_QNAME = new QName("http://www.delta.notification",
        "sendSMSResponse");
    private final static QName _SendEmailResponse_QNAME = new QName("http://www.delta.notification",
        "sendEmailResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: com.delta.model.ws
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Response }
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link SendSMSRequest }
     */
    public SendSMSRequest createSendSMSRequest() {
        return new SendSMSRequest();
    }

    /**
     * Create an instance of {@link SendNotificationRequest }
     */
    public SendNotificationRequest createSendNotificationRequest() {
        return new SendNotificationRequest();
    }

    /**
     * Create an instance of {@link SendEmailRequest }
     */
    public SendEmailRequest createSendEmailRequest() {
        return new SendEmailRequest();
    }

    /**
     * Create an instance of {@link EmailAttachment }
     */
    public EmailAttachment createEmailAttachment() {
        return new EmailAttachment();
    }

    /**
     * Create an instance of {@link EmailAuthenticationRequest }
     */
    public EmailAuthenticationRequest createEmailAuthenticationRequest() {
        return new EmailAuthenticationRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.delta.notification", name = "sendNotificationResponse")
    public JAXBElement<Response> createSendNotificationResponse(Response value) {
        return new JAXBElement<Response>(_SendNotificationResponse_QNAME, Response.class, null,
            value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.delta.notification", name = "emailAuthenticationResponse")
    public JAXBElement<Response> createEmailAuthenticationResponse(Response value) {
        return new JAXBElement<Response>(_EmailAuthenticationResponse_QNAME, Response.class, null,
            value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.delta.notification", name = "sendSMSResponse")
    public JAXBElement<Response> createSendSMSResponse(Response value) {
        return new JAXBElement<Response>(_SendSMSResponse_QNAME, Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.delta.notification", name = "sendEmailResponse")
    public JAXBElement<Response> createSendEmailResponse(Response value) {
        return new JAXBElement<Response>(_SendEmailResponse_QNAME, Response.class, null, value);
    }

}
