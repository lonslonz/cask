package com.skplanet.cask.test.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="testcase")
@XmlAccessorType(XmlAccessType.FIELD)
public class TCase {
    @XmlAttribute
    private String classname;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private float time;
    
    public float getTime() {
        return time;
    }
    public void setTime(float time) {
        this.time = time;
    }
    public String getClassname() {
        return classname;
    }
    public void setClassname(String classname) {
        this.classname = classname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
