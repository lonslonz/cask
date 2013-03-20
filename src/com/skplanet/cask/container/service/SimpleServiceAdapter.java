package com.skplanet.cask.container.service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.ServiceRuntimeRegistry;
import com.skplanet.cask.container.model.SimpleParams;
import com.skplanet.cask.util.StringUtil;


public class SimpleServiceAdapter extends AbstractController implements ServiceAdapter {
    Logger logger = LoggerFactory.getLogger(SimpleServiceAdapter.class);
   
    SimpleService service;
    
    public void setService(SimpleService service) {
       this.service = service;
    }
    
    
    @Override
    public void setService(Object serviceAdapter, Object service) {
        ((SimpleServiceAdapter)serviceAdapter).setService((SimpleService)service);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        try {
            if(!(request.getMethod().toUpperCase().equals("POST") || request.getMethod().toUpperCase().equals("GET"))) {
                throw new Exception("not supoorted http method : " + request.getMethod());
            }
            
            if(service == null) {
                throw new Exception("service not set");
            }
            
            ServiceRuntimeInfo runtimeInfo = 
                    ServiceRuntimeRegistry.getInstance().getServiceRuntimeInfo(request.getPathInfo());
            
            if(!request.getMethod().toUpperCase().equals(runtimeInfo.getMethod().toUpperCase())) {
                throw new Exception("not supported http method : " + request.getMethod());
            }
            
            SimpleParams sRequest = new SimpleParams();
            SimpleParams sResponse = new SimpleParams();
            
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> mapperMap = null;
            Object mapperObj = null;
            
            if(request.getMethod().equals("POST")) {
                if(runtimeInfo.getInClass() != null) {
                    mapperObj = mapper.readValue(request.getInputStream(), Class.forName(runtimeInfo.getInClass()));
                } else {
                    mapperMap = mapper.readValue(request.getInputStream(), Map.class);
                }
            } 
            else {
                mapperMap = new HashMap<String, Object>();
                makeParams(request, mapperMap);
            }
            
            sRequest.setParams(mapperMap);
            sRequest.setModelObject(mapperObj);
            
            service.handle(sRequest, sResponse, runtimeInfo);
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            
            if(runtimeInfo.getOutClass() != null) {
                mapper.writeValue(response.getOutputStream(), sResponse.getModelObject());
            } else {
                mapper.writeValue(response.getOutputStream(), sResponse.getParams());    
            }
            
        } catch (Exception e) {
            
            logger.error(StringUtil.exception2Str(e));
            
            throw e;
        } finally {
            
        }
        
        return null;
    }
    
    private void makeParams(HttpServletRequest request, Map<String, Object>dest) {
        Enumeration<String> names = request.getParameterNames();
        while(names.hasMoreElements()) {
            String key = names.nextElement();
            String value = request.getParameter(key);
            dest.put(key, value);
        }
    }
}
