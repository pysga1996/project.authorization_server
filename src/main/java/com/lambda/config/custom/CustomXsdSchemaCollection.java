package com.lambda.config.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;
import java.io.IOException;

@Component("xsdSchemaCollection")
public class CustomXsdSchemaCollection implements XsdSchemaCollection {

    private static final Logger logger = LogManager.getLogger(CustomXsdSchemaCollection.class);

    private final Resource[] resources;

    private final XsdSchema[] schemas;

    public CustomXsdSchemaCollection(Resource[] resources, XsdSchema[] schemas) {
        this.resources = resources;
        this.schemas = schemas;
    }

    @Override
    public XsdSchema[] getXsdSchemas() {
        return schemas;
    }

    @Override
    public XmlValidator createValidator() {
        try {
            return XmlValidatorFactory.createValidator(resources,
                    "http://www.w3.org/2001/XMLSchema");
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }
}
