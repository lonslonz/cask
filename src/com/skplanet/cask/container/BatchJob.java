package com.skplanet.cask.container;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.config.ServerInfo;
import com.skplanet.cask.util.HttpUtil;
import com.skplanet.cask.util.StringUtil;

public class BatchJob extends Thread {
	
    private Logger logger = LoggerFactory.getLogger(BatchJob.class);
    
	private String httpMethod;
	private String url;
	private String inClass;
	private String outClass;
	private Integer sleepMSec;
	
	public BatchJob(String httpMethod, String url, String inClass, String outClass, Integer sleepMSec) {
		this.httpMethod = httpMethod;
		this.url  = url;
		this.inClass = inClass;
		this.outClass = outClass;
		this.sleepMSec = sleepMSec;
	}
	@Override
    public String toString() {
		return String.format("url(%s), method(%s)", url, httpMethod);
	}
	@Override
    public void run() {
		
		try {
		    boolean result;
		    String message;
	
		    logger.info("batch job started : {}", url);
	
			Map<String, Object> resp = callSimpleService(url);
			result = Boolean.parseBoolean((String)resp.get("return"));
			message = (String)resp.get("message");
			
			if(result) {
                logger.info("success : batch job complete : ", this.toString());
            }
            else {
                logger.error("batch job failed from http request : " + this.toString());
                logger.error("error : " + message);
            }
			
		} catch (Exception e) {
			logger.error("batch job failed from local execution : " + this.toString());
			logger.error("error : " + StringUtil.exception2Str(e));
		}
	}

    
    private Map<String, Object> callSimpleService(String serviceUrl) throws Exception {
        ServerInfo serverInfo = ConfigReader.getInstance().getServerConfig().getServerInfo();
        String url = serverInfo.getHttpServerAddr() + serviceUrl;
        
        ObjectMapper mapper = new ObjectMapper();
        String content = null;       
        
        Map<String, Object> mapperMap = new HashMap<String, Object>();
        mapperMap.put("id", "batch");
        mapperMap.put("sleepMSec", sleepMSec);
        content = mapper.writeValueAsString(mapperMap);
    
        Map<String, Object> resp = HttpUtil.sendSimpleHttpPut(url, content, null);
        return resp;
    }
    
}
