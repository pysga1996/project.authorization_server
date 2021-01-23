
package com.lambda.dto.soap.user;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResponseType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResponseType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *     &lt;enumeration value="1"/>
 *     &lt;enumeration value="2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResponseType")
@XmlEnum(Integer.class)
public enum ResponseType {

    @XmlEnumValue("1")
    SINGLE(1),
    @XmlEnumValue("2")
    LIST(2);
    private final int value;

    ResponseType(int v) {
        value = v;
    }

    public int value() {
        return value;
    }

    public static ResponseType fromValue(int v) {
        for (ResponseType c: ResponseType.values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }

}
