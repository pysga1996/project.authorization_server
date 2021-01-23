
package com.lambda.dto.soap.client;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="client" type="{http://www.lambda.authorization/clients}ClientDTO"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "client"
})
@XmlRootElement(name = "CreateClientRequest")
public class CreateClientRequest
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected ClientDTO client;

    /**
     * Gets the value of the client property.
     * 
     * @return
     *     possible object is
     *     {@link ClientDTO }
     *     
     */
    public ClientDTO getClient() {
        return client;
    }

    /**
     * Sets the value of the client property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientDTO }
     *     
     */
    public void setClient(ClientDTO value) {
        this.client = value;
    }

    public boolean isSetClient() {
        return (this.client!= null);
    }

}
