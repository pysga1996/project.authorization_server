
package com.lambda.dto.soap.client;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;


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
 *         &lt;element name="clientList">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="client" type="{http://www.lambda.authorization/clients}ClientDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "client",
    "clientList"
})
@XmlRootElement(name = "GetClientResponse")
public class GetClientResponse
    implements Serializable
{

    @XmlElement(required = true)
    protected ClientDTO client;
    @XmlElementWrapper(name = "clientList")
    @XmlElement(name = "client")
    protected List<ClientDTO> clientList;

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

    /**
     * Gets the value of the clientList property.
     * 
     * @return
     *     possible object is
     *     {@link List<ClientDTO> }
     *     
     */
    public List<ClientDTO> getClientList() {
        return clientList;
    }

    /**
     * Sets the value of the clientList property.
     * 
     * @param value
     *     allowed object is
     *     {@link List<ClientDTO> }
     *     
     */
    public void setClientList(List<ClientDTO> value) {
        this.clientList = value;
    }

    public boolean isSetClientList() {
        return (this.clientList!= null);
    }

}
