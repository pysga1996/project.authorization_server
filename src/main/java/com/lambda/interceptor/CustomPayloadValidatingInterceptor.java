package com.lambda.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.xml.xsd.XsdSchemaCollection;

@Component
public class CustomPayloadValidatingInterceptor extends PayloadValidatingInterceptor {

    @Autowired
    public CustomPayloadValidatingInterceptor(XsdSchemaCollection xsdSchemaCollection) {
        this.setXsdSchemaCollection(xsdSchemaCollection);
    }
}
