package com.skplanet.cask.container.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.skplanet.cask.util.StringUtil;



@XmlRootElement(name="service")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceInfo {
    
    @XmlAttribute(name="url")
    String url = null;
    @XmlAttribute(name="class")
    String className = null;
    @XmlAttribute(name="exec")
    String execType = "request";
    @XmlAttribute(name="mbean")
    String mbeanClassName = null;
    @XmlAttribute(name="method")
    String method = "post";
    @XmlAttribute(name="inClass")
    String inClass = null;
    @XmlAttribute(name="outClass")
    String outClass = null;
    @XmlAttribute(name="sleepMSec")
    Integer sleepMSec = null;

    

    public String getUrl() {   
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }


    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }
    
    public String getClassBeanName() {
        return className + "Impl"; 
    }

    public String getMBeanClassName() {
        return mbeanClassName;
    }

    public void setMBeanClassName(String mbeanClassName) {
        this.mbeanClassName = mbeanClassName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    @Override
    public String toString() {
        
        return StringUtil.class2Str(this); 
    }

    public String getInClass() {
        return inClass;
    }

    public void setInClass(String inClass) {
        this.inClass = inClass;
    }

    public String getOutClass() {
        return outClass;
    }

    public void setOutClass(String outClass) {
        this.outClass = outClass;
    }


    public Integer getSleepMSec() {
        return sleepMSec;
    }

    public void setSleepMSec(Integer sleepSec) {
        this.sleepMSec = sleepSec;
    }

    
}
