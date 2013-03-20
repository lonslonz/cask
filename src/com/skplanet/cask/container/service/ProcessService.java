package com.skplanet.cask.container.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.dao.ServiceRuntimeDao;
import com.skplanet.cask.container.model.SimpleParams;


public class ProcessService implements SimpleService  {
    
    private Logger logger = LoggerFactory.getLogger(ProcessService.class);

    @Override
    public void handle(SimpleParams request, 
                       SimpleParams response, 
                       ServiceRuntimeInfo runtimeInfo) throws Exception  {
        
        logger.info("Start : process service \n request : {}", request.getParams());
        
        long start = System.currentTimeMillis();
        
        String server = request.getString("server");
        String id = request.getString("id");
        String url = request.getString("url");
        String status = request.getString("status");
        String serverId = request.getString("serverId");
//        if(status == null) {
//            status = ServerStatus.RUNNING.getLowerStr();
//        }
        
        ServiceRuntimeDao serviceDao = new ServiceRuntimeDao();
        Map<String, Object> resultMap = serviceDao.select(server, status, id, url, serverId);
        response.setParams(resultMap);
        
        logger.info("End : process service, elapsed : {}, response : {}", 
                    (System.currentTimeMillis() - start)/1000,
                    resultMap);
    }    
}

