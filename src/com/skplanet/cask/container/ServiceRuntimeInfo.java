package com.skplanet.cask.container;

public class ServiceRuntimeInfo {
    
    private String url;
    private Object mbeanObj;
    private Integer serverDbPkId;
    private String method;
    private String inClass;
    private String outClass;
    
    public ServiceRuntimeInfo(String url, Object mbeanObj, Integer serverDbPkId, String method, String inClass, String outClass) {
        this.url = url;
        this.mbeanObj = mbeanObj;
        this.serverDbPkId = serverDbPkId;
        this.method = method;
        this.inClass = inClass;
        this.outClass = outClass;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMBeanObj(Object mbeanObj) {
        this.mbeanObj = mbeanObj;
    }
    public Integer getServiceRuntimeId() {
        return serverDbPkId;
    }
    public void setServiceRuntimeId(Integer serviceRuntimeId) {
        this.serverDbPkId = serviceRuntimeId;
    }
    
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    @SuppressWarnings("unchecked")
    public <T> T getMBeanObj() {
        if(mbeanObj == null) {
            return null;
        }
        else {
            return (T)mbeanObj;
        }
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

}
