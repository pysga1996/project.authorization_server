
package com.lambda.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


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
 *         &lt;element name="type" type="{http://www.lambda.authorization}ResponseType"/>
 *         &lt;element name="user" type="{http://www.lambda.authorization}User"/>
 *         &lt;element name="userList">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="user" type="{http://www.lambda.authorization}User" maxOccurs="unbounded" minOccurs="0"/>
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
        "type",
        "user",
        "userList"
})
@XmlRootElement(name = "UserResponse")
public class UserResponse
        implements Serializable
{

    @XmlElement(required = true)
    protected ResponseType type;
    @XmlElement(required = true)
    protected User user;
    @XmlElementWrapper(name = "userList")
    @XmlElement(required = true)
    protected List<User> userList;

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link ResponseType }
     *
     */
    public ResponseType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link ResponseType }
     *
     */
    public void setType(ResponseType value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the user property.
     *
     * @return
     *     possible object is
     *     {@link User }
     *
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     *
     * @param value
     *     allowed object is
     *     {@link User }
     *
     */
    public void setUser(User value) {
        this.user = value;
    }

    public boolean isSetUser() {
        return (this.user!= null);
    }

    /**
     * Gets the value of the userList property.
     *
     * @return
     *     possible object is
     *     {@link List<User> }
     *
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * Sets the value of the userList property.
     *
     * @param value
     *     allowed object is
     *     {@link List<User> }
     *
     */
    public void setUserList(List<User> value) {
        this.userList = value;
    }

    public boolean isSetUserList() {
        return (this.userList!= null);
    }

}
