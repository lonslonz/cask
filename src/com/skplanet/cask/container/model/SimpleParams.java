package com.skplanet.cask.container.model;


import java.util.Map;


public class SimpleParams {
    
    private Map<String, Object> params = null;
    private Object modelObject = null;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    public Object get(String key) {
        return params.get(key);
    }
    
    public String getString(String key) {
        Object value = params.get(key);
        if(value == null) {
            return null;
        }
        else {
            return (String)params.get(key);
        }
    }

    

    public Object getModelObject() {
        return modelObject;
    }

    public void setModelObject(Object modelObject) {
        this.modelObject = modelObject;
    }

    @SuppressWarnings("unchecked")
    public <T> T getModelObjectByType() {
        if(modelObject == null) {
            return null;
        }
        else {
            return (T)modelObject;
        }
    } 
 
}
