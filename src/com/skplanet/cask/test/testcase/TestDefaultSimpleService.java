package com.skplanet.cask.test.testcase;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.model.SimpleParams;
import com.skplanet.cask.container.service.SimpleService;

public class TestDefaultSimpleService implements SimpleService {
    
    
    private Logger logger = LoggerFactory.getLogger(TestDefaultSimpleService.class);
    
    @Override
    public void handle(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {

        logger.info("" + request);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("returnCode",  0);
        resultMap.put("returnDesc",  "success");
        response.setParams(resultMap);
        
    }
}
