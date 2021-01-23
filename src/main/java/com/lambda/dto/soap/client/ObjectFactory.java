
package com.lambda.dto.soap.client;

import com.lambda.dto.soap.user.AuthorityDTO;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lambda.dto.soap package. 
 * &lt;p&gt;An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lambda.dto.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetClientResponse }
     * 
     */
    public GetClientResponse createGetClientResponse() {
        return new GetClientResponse();
    }

    /**
     * Create an instance of {@link InfoMapDTO }
     * 
     */
    public InfoMapDTO createInfoMapDTO() {
        return new InfoMapDTO();
    }

    /**
     * Create an instance of {@link CreateClientRequest }
     * 
     */
    public CreateClientRequest createCreateClientRequest() {
        return new CreateClientRequest();
    }

    /**
     * Create an instance of {@link ClientDTO }
     * 
     */
    public ClientDTO createClientDTO() {
        return new ClientDTO();
    }

    /**
     * Create an instance of {@link CreateClientResponse }
     * 
     */
    public CreateClientResponse createCreateClientResponse() {
        return new CreateClientResponse();
    }

    /**
     * Create an instance of {@link GetClientRequest }
     * 
     */
    public GetClientRequest createGetClientRequest() {
        return new GetClientRequest();
    }

    /**
     * Create an instance of {@link AuthorityDTO }
     * 
     */
    public AuthorityDTO createAuthorityDTO() {
        return new AuthorityDTO();
    }

    /**
     * Create an instance of {@link InfoMapDTO.Entry }
     * 
     */
    public InfoMapDTO.Entry createInfoMapDTOEntry() {
        return new InfoMapDTO.Entry();
    }

}
