package com.skplanet.cask.container.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.skplanet.cask.container.config.ServerInfo;
import com.skplanet.cask.container.config.ServiceInfo;


@XmlRootElement(name="configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerConfig {
    private static final String RUNTIME_DB = "runtimeDb";
    private static final String SAVE_RUNTIME = "saveRuntimeIntoDb";
    
    @XmlElement(name="server")
    private ServerInfo serverInfo = null;
    
    @XmlElementWrapper(name = "services")
    @XmlElement(name="service")
    private List<ServiceInfo> serviceInfoList = null;
    
    @XmlElementWrapper(name = "datasources")
    @XmlElement(name = "datasource")
    private List<DataSourceInfo> dataSourceInfoList = null;
    
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<Property> properties = null;
    
    private Map<String, Property> propMap= null;
    
    public ServerInfo getServerInfo() {
        return serverInfo;
    }
    public void setServerInfo(ServerInfo serverInfo) {
        if(serverInfo != null) {
            this.serverInfo = serverInfo;
        }
    }
    
    public List<ServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }
    public void setServiceInfoList(List<ServiceInfo> serviceInfoList) {
        if(serviceInfoList != null) {
            this.serviceInfoList = serviceInfoList;
        }
    }
    
    public List<DataSourceInfo> getDataSourceInfoList() {
        return dataSourceInfoList;
    }
    public void setDataSourceInfoList(List<DataSourceInfo> dataSourceInfoList) {
        this.dataSourceInfoList = dataSourceInfoList;
    }
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
    public void makePropMap() {
        if(properties == null || propMap != null) {
            return;
        }
        propMap = new HashMap<String, Property>();
        
        Iterator<Property> it = properties.iterator();
        while(it.hasNext()) {
            Property prop = it.next();
            propMap.put(prop.getKey(), prop);
        }
    }
    public String getPropValue(String key) {
        if(propMap == null) {
            return null;
        }
        if(propMap.get(key) == null) {
            return null;
        }
        return propMap.get(key).getValue();
    }
    public boolean saveRuntimeIntoDb() {
        Boolean saveRuntime = false;
        if(propMap != null) { 
            saveRuntime = Boolean.parseBoolean(getPropValue(SAVE_RUNTIME));
        }
        return saveRuntime;
    }
    public String getRuntimeDb() {
        return getPropValue(RUNTIME_DB);
    }
}
