
package com.lambda.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for User complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="User">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorities">
 *           &lt;simpleType>
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="avatarUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="setting" type="{http://www.lambda.authorization}Setting"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "username",
    "password",
    "authorities",
    "avatarUrl",
    "setting"
})
public class User
    implements Serializable
{

    @XmlElement(required = true)
    protected String username;
    @XmlElement(required = true)
    protected String password;
    @XmlList
    @XmlElement(required = true)
    protected List<String> authorities;
    @XmlElement(required = true)
    protected String avatarUrl;
    @XmlElement(required = true)
    protected Setting setting;
    @XmlAttribute(name = "id")
    protected Byte id;

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    public boolean isSetUsername() {
        return (this.username!= null);
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    public boolean isSetPassword() {
        return (this.password!= null);
    }

    /**
     * Gets the value of the authorities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthorities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<String>();
        }
        return this.authorities;
    }

    public boolean isSetAuthorities() {
        return ((this.authorities!= null)&&(!this.authorities.isEmpty()));
    }

    public void unsetAuthorities() {
        this.authorities = null;
    }

    /**
     * Gets the value of the avatarUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Sets the value of the avatarUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvatarUrl(String value) {
        this.avatarUrl = value;
    }

    public boolean isSetAvatarUrl() {
        return (this.avatarUrl!= null);
    }

    /**
     * Gets the value of the setting property.
     * 
     * @return
     *     possible object is
     *     {@link Setting }
     *     
     */
    public Setting getSetting() {
        return setting;
    }

    /**
     * Sets the value of the setting property.
     * 
     * @param value
     *     allowed object is
     *     {@link Setting }
     *     
     */
    public void setSetting(Setting value) {
        this.setting = value;
    }

    public boolean isSetSetting() {
        return (this.setting!= null);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public byte getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setId(byte value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

    public void unsetId() {
        this.id = null;
    }

}
