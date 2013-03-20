package com.skplanet.cask.container.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute
    private String key = null;
    @XmlAttribute
    private String value = null;
    
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return key + " = " + value;
    }
    
}
