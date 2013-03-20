package com.skplanet.cask.container.model;

import java.util.HashMap;
import java.util.Iterator;

public class InParams {
    
    private String id;
    
    private HashMap<String, Object> params = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        
        StringBuilder buf = new StringBuilder();
        buf.append("id : " + id +  ", ");
        
        if(params != null) {
            buf.append("params : ");
            
            Iterator<String> it = params.keySet().iterator();
            
            while(it.hasNext()) {
                String key = it.next();
                buf.append(key + "(" + params.get(key) + "), ");
            }
        }
        return buf.toString();
    }
}
