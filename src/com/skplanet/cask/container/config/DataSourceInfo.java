package com.skplanet.cask.container.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="datasource")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSourceInfo {
    
    @XmlAttribute
    private String id;
    @XmlElement
    private String driverClassName;
    @XmlElement
    private String url;
    @XmlElement
    private String username;
    @XmlElement 
    private String password;
    @XmlElement 
    private String initSQL;
    @XmlElement 
    private int initialSize;
    @XmlElement 
    private int minIdle;
    @XmlElement 
    private int maxIdle;
    @XmlElement 
    private int maxActive;
    @XmlElement 
    private boolean defaultAutoCommit;
    @XmlElement 
    private String validateQuery;
    
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDriverClassName() {
        return driverClassName;
    }
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getInitSQL() {
        return initSQL;
    }
    public void setInitSQL(String initSQL) {
        this.initSQL = initSQL;
    }
    public int getInitialSize() {
        return initialSize;
    }
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }
    public int getMinIdle() {
        return minIdle;
    }
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
    public int getMaxIdle() {
        return maxIdle;
    }
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    public int getMaxActive() {
        return maxActive;
    }
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }
    public boolean isDefaultAutoCommit() {
        return defaultAutoCommit;
    }
    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }
    public String getValidateQuery() {
        return validateQuery;
    }
    public void setValidateQuery(String validateQuery) {
        this.validateQuery = validateQuery;
    }
    
    
}
