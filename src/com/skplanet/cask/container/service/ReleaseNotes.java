package com.skplanet.cask.container.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.model.SimpleParams;


public class ReleaseNotes implements SimpleService  {
    
    private Logger logger = LoggerFactory.getLogger(ReleaseNotes.class);
    private String RELEASE_NOTES = ConfigReader.getInstance().getHome() + "/RELEASE_NOTES.txt";

    @Override
    public void handle(SimpleParams request, 
                       SimpleParams response, 
                       ServiceRuntimeInfo runtimeInfo) throws Exception  {
        
        HashMap<String, Object> resultMap = new HashMap<String,Object>();
        
        FileReader fr = new FileReader(RELEASE_NOTES); 
        BufferedReader br = new BufferedReader(fr);
        
        StringBuilder sb = new StringBuilder();
        
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
            //sb.append("<BR>");
        }
        
        String releaseNotes = sb.toString();
        resultMap.put("releaseNotes", releaseNotes);
        response.setParams(resultMap);
        
        logger.info("releaseNotes : {}", releaseNotes);
        
        br.close();
        fr.close();
        
    }    
}

