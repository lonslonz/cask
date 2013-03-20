package com.skplanet.cask.container.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.skplanet.cask.util.StringUtil;

@XmlRootElement(name="server")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerInfo {
    
    @XmlAttribute
    private String name = null;
    @XmlAttribute
    private Integer port = null;
    @XmlAttribute
    private Integer maxThreads = null;
    @XmlAttribute
    private String contextPath = null;
    @XmlAttribute
    private String servicePath = null;
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public Integer getMaxThreads() {
        return maxThreads;
    }
    public void setMaxTheads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }
    
    public static void copyIfNull(ServerInfo from, ServerInfo to) {
        if(to.getName() == null) {
            to.setName(from.getName());
        }
        if(to.getPort() == null) {
            to.setPort(from.getPort());
        }
        if(to.getMaxThreads() == null) {
            to.setMaxTheads(from.getMaxThreads());
        }
        if(to.getContextPath() == null) {
            to.setContextPath(from.getContextPath());
        }
        if(to.getServicePath() == null) {
            to.setServicePath(from.getServicePath());
        }
    }
    @Override
    public String toString() {
        return StringUtil.class2Str(this);
    }

    public String getContextPath() {
        return contextPath;
    }
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
    public String getServicePath() {
        return servicePath;
    }
    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }
    public String getHttpServerAddr() {
        return "http://localhost:" + getPort() + "/" + getContextPath() + "/" + getServicePath(); 
    }
   
}
