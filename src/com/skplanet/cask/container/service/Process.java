package com.skplanet.cask.container.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServerStatus;
import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.dao.ServerRuntimeDao;
import com.skplanet.cask.container.model.SimpleParams;


public class Process implements SimpleService  {
    
    private Logger logger = LoggerFactory.getLogger(Process.class);

    @Override
    public void handle(SimpleParams request, 
                       SimpleParams response, 
                       ServiceRuntimeInfo runtimeInfo) throws Exception  {
        
        logger.info("Start : process \n request : {}", request.getParams());
        
        long start = System.currentTimeMillis();
        
        String server = request.getString("server");
        String id = request.getString("id");
        String status = request.getString("status");
        if(status == null) {
            status = ServerStatus.RUNNING.getLowerStr();
        }
        
        ServerRuntimeDao serverDao = new ServerRuntimeDao();
        Map<String, Object> resultMap = serverDao.select(server, status, id);
        response.setParams(resultMap);
        
        
        logger.info("End : process, elapsed : {}, response : {}", (System.currentTimeMillis() - start)/1000,
                                                                   resultMap);
    }    
}

