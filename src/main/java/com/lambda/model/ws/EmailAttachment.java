package com.lambda.model.ws;

import java.io.Serializable;
import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EmailAttachment complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EmailAttachment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="file" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmailAttachment", propOrder = {
    "file",
    "fileName"
})
public class EmailAttachment
    implements Serializable {

    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler file;
    @XmlElement(required = true)
    protected String fileName;

    /**
     * Gets the value of the file property.
     *
     * @return possible object is {@link DataHandler }
     */
    public DataHandler getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     *
     * @param value allowed object is {@link DataHandler }
     */
    public void setFile(DataHandler value) {
        this.file = value;
    }

    public boolean isSetFile() {
        return (this.file != null);
    }

    /**
     * Gets the value of the fileName property.
     *
     * @return possible object is {@link String }
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     *
     * @param value allowed object is {@link String }
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    public boolean isSetFileName() {
        return (this.fileName != null);
    }

}
