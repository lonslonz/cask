package com.skplanet.cask.test.testcase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.model.SimpleParams;
import com.skplanet.cask.container.service.SimpleService;

public class AddTestSimpleService implements SimpleService {
    
    Logger logger = LoggerFactory.getLogger(AddTestSimpleService.class);
    
    @Override
    public void handle(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {
        
        Map<String, Object> data = request.getParams();
        Iterator<String> it = data.keySet().iterator();
        
        while(it.hasNext()) {
            String me =  it.next();
            Object obj = data.get(me);
            logger.debug("key : {}, value : {}", me, obj);
            if(me.equals("params")) {
                Object o = data.get(me);
                logger.debug("" + o.getClass());
            }
        }
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("returnCode",  0);
        resultMap.put("returnDesc",  "success");
        response.setParams(resultMap);
        
        logger.info("simple service response : {} ", response.getParams());
    }
}
