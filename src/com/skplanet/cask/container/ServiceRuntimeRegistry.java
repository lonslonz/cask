package com.skplanet.cask.container;

import java.util.HashMap;

public class ServiceRuntimeRegistry {
    
    private HashMap<String, ServiceRuntimeInfo> runtimeMap = new HashMap<String, ServiceRuntimeInfo> ();
    private static ServiceRuntimeRegistry instance = new ServiceRuntimeRegistry();
    
    public static ServiceRuntimeRegistry getInstance() {
        if(instance == null) {
            instance = new ServiceRuntimeRegistry();
        }
        return instance;
    }
    public static void close() {
        if(instance != null) {
            instance = null;
        }
    }
    
    public void addServiceRuntimeInfo(
            String key, 
            Object mbean, 
            Integer serviceRuntimeId, 
            String method, 
            String inClass,
            String outClass) throws Exception {
        
        ServiceRuntimeInfo exists = runtimeMap.get(key);
        if(exists != null) {
            throw new Exception("Can't add to serviceRuntimeRegistry. already exsists : " + key);
        } else {
        
            ServiceRuntimeInfo info = new ServiceRuntimeInfo(key, mbean, serviceRuntimeId, method, inClass, outClass);
            runtimeMap.put(key, info);
        }
    }
    
    public ServiceRuntimeInfo getServiceRuntimeInfo(String key) throws Exception {
        ServiceRuntimeInfo runtimeInfo = runtimeMap.get(key);
        if(runtimeMap.get(key) == null) {
            throw new Exception("ServiceRuntimeInfo not exists : " + key);
        }
        return runtimeInfo;
    }
}
