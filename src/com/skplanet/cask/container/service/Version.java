package com.skplanet.cask.container.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.model.SimpleParams;


public class Version implements SimpleService  {
    
    private Logger logger = LoggerFactory.getLogger(Version.class);

    @Override
    public void handle(SimpleParams request, 
                       SimpleParams response, 
                       ServiceRuntimeInfo runtimeInfo) throws Exception  {
        
        HashMap<String, Object> resultMap = new HashMap<String,Object>();
        
        resultMap.put("version", ConfigReader.getInstance().getVersion());
        response.setParams(resultMap);
        
        logger.info("version : {}", ConfigReader.getInstance().getVersion());
    }    
}

