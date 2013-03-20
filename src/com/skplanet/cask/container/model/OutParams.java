package com.skplanet.cask.container.model;

import java.util.HashMap;
import java.util.Iterator;

public class OutParams {

    private String id = null;
    private Boolean returnCode = null; 
    private String returnMessage = null;
    private HashMap<String, Object> results = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Boolean returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public HashMap<String, Object> getResults() {
        return results;
    }

    public void setResults(HashMap<String, Object> results) {
        this.results = results;
    }
    
    @Override
    public String toString() {
        
        StringBuilder buf = new StringBuilder();
        buf.append("id : " + id +  ", " +   
                    "returnCode : " + returnCode + ", " + 
                    "returnMessage : " + returnMessage + ", ");
        
        
        
        if(results != null) {
            buf.append("results : ");
            Iterator<String> it = results.keySet().iterator();
            
            while(it.hasNext()) {
                String key = it.next();
                buf.append(key + " : " + results.get(key));
            }
        }
        return buf.toString();
    }
}
