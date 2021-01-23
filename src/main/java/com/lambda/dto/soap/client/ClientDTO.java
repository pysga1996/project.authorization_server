
package com.lambda.dto.soap.client;

import com.lambda.constant.WebServiceConstant;
import com.lambda.dto.soap.user.AuthorityDTO;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>Java class for ClientDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClientDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clientSecret" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="scope">
 *           &lt;simpleType>
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resourceIds">
 *           &lt;simpleType>
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="authorizedGrantTypes">
 *           &lt;simpleType>
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="registeredRedirectUris">
 *           &lt;simpleType>
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="autoApproveScopes">
 *           &lt;simpleType>
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="authorities">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="authority" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="asd"/>
 *                         &lt;enumeration value="wqewq"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="accessTokenValiditySeconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="refreshTokenValiditySeconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="additionalInformation" type="{http://www.lambda.authorization/clients}InfoMap"/>
 *       &lt;/sequence>
 *       &lt;attribute name="clientId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientDTO", propOrder = {
    "clientSecret",
    "scope",
    "resourceIds",
    "authorizedGrantTypes",
    "registeredRedirectUris",
    "autoApproveScopes",
    "authorities",
    "accessTokenValiditySeconds",
    "refreshTokenValiditySeconds",
    "additionalInformation"
})
@XmlRootElement(name = "ClientDTO", namespace = WebServiceConstant.CLIENTS_NAMESPACE)
public class ClientDTO
    implements Serializable
{

    @XmlElement(required = true)
    protected String clientSecret;
    @XmlList
    @XmlElement(required = false)
    protected List<String> scope;
    @XmlList
    @XmlElement(required = false)
    protected List<String> resourceIds;
    @XmlList
    @XmlElement(required = false)
    protected List<String> authorizedGrantTypes;
    @XmlList
    @XmlElement(required = false)
    protected List<String> registeredRedirectUris;
    @XmlList
    @XmlElement(required = false)
    protected List<String> autoApproveScopes;
    @XmlElementWrapper(name = "authorities")
    @XmlElement(name = "authority")
    protected List<AuthorityDTO> authorities;
    protected int accessTokenValiditySeconds;
    protected int refreshTokenValiditySeconds;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(value = InfoMapAdapter.class, type = InfoMapDTO.class)
    protected Map<String, Object> additionalInformation;
    @XmlAttribute(name = "clientId")
    protected String clientId;

    /**
     * Gets the value of the clientSecret property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the value of the clientSecret property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientSecret(String value) {
        this.clientSecret = value;
    }

    public boolean isSetClientSecret() {
        return (this.clientSecret!= null);
    }

    /**
     * Gets the value of the scope property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scope property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScope().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getScope() {
        if (scope == null) {
            scope = new ArrayList<String>();
        }
        return this.scope;
    }

    public boolean isSetScope() {
        return ((this.scope!= null)&&(!this.scope.isEmpty()));
    }

    public void unsetScope() {
        this.scope = null;
    }

    /**
     * Gets the value of the resourceIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResourceIds() {
        if (resourceIds == null) {
            resourceIds = new ArrayList<String>();
        }
        return this.resourceIds;
    }

    public boolean isSetResourceIds() {
        return ((this.resourceIds!= null)&&(!this.resourceIds.isEmpty()));
    }

    public void unsetResourceIds() {
        this.resourceIds = null;
    }

    /**
     * Gets the value of the authorizedGrantTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorizedGrantTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthorizedGrantTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuthorizedGrantTypes() {
        if (authorizedGrantTypes == null) {
            authorizedGrantTypes = new ArrayList<String>();
        }
        return this.authorizedGrantTypes;
    }

    public boolean isSetAuthorizedGrantTypes() {
        return ((this.authorizedGrantTypes!= null)&&(!this.authorizedGrantTypes.isEmpty()));
    }

    public void unsetAuthorizedGrantTypes() {
        this.authorizedGrantTypes = null;
    }

    /**
     * Gets the value of the registeredRedirectUris property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registeredRedirectUris property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegisteredRedirectUris().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRegisteredRedirectUris() {
        if (registeredRedirectUris == null) {
            registeredRedirectUris = new ArrayList<String>();
        }
        return this.registeredRedirectUris;
    }

    public boolean isSetRegisteredRedirectUris() {
        return ((this.registeredRedirectUris!= null)&&(!this.registeredRedirectUris.isEmpty()));
    }

    public void unsetRegisteredRedirectUris() {
        this.registeredRedirectUris = null;
    }

    /**
     * Gets the value of the autoApproveScopes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the autoApproveScopes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAutoApproveScopes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAutoApproveScopes() {
        if (autoApproveScopes == null) {
            autoApproveScopes = new ArrayList<String>();
        }
        return this.autoApproveScopes;
    }

    public boolean isSetAutoApproveScopes() {
        return ((this.autoApproveScopes!= null)&&(!this.autoApproveScopes.isEmpty()));
    }

    public void unsetAutoApproveScopes() {
        this.autoApproveScopes = null;
    }

    /**
     * Gets the value of the authorities property.
     * 
     * @return
     *     possible object is
     *     {@link List<AuthorityDTO> }
     *     
     */
    public List<AuthorityDTO> getAuthorities() {
        return authorities;
    }

    /**
     * Sets the value of the authorities property.
     * 
     * @param value
     *     allowed object is
     *     {@link List<AuthorityDTO> }
     *     
     */
    public void setAuthorities(List<AuthorityDTO> value) {
        this.authorities = value;
    }

    public boolean isSetAuthorities() {
        return (this.authorities!= null);
    }

    /**
     * Gets the value of the accessTokenValiditySeconds property.
     * 
     */
    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    /**
     * Sets the value of the accessTokenValiditySeconds property.
     * 
     */
    public void setAccessTokenValiditySeconds(int value) {
        this.accessTokenValiditySeconds = value;
    }

    public boolean isSetAccessTokenValiditySeconds() {
        return true;
    }

    /**
     * Gets the value of the refreshTokenValiditySeconds property.
     * 
     */
    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    /**
     * Sets the value of the refreshTokenValiditySeconds property.
     * 
     */
    public void setRefreshTokenValiditySeconds(int value) {
        this.refreshTokenValiditySeconds = value;
    }

    public boolean isSetRefreshTokenValiditySeconds() {
        return true;
    }

    /**
     * Gets the value of the additionalInformation property.
     * 
     * @return
     *     possible object is
     *     {@link InfoMapDTO }
     *     
     */
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Sets the value of the additionalInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoMapDTO }
     *     
     */
    public void setAdditionalInformation(Map<String, Object> value) {
        this.additionalInformation = value;
    }

    public boolean isSetAdditionalInformation() {
        return (this.additionalInformation!= null);
    }

    /**
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    public boolean isSetClientId() {
        return (this.clientId!= null);
    }

}
