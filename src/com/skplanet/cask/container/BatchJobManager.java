package com.skplanet.cask.container;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.config.ServiceInfo;


public class BatchJobManager {
    
    private static BatchJobManager instance = new BatchJobManager();
    private List<BatchJob> jobList = new ArrayList<BatchJob>();
    
    public static BatchJobManager getInstance() {
        if(instance == null) {
            instance = new BatchJobManager();
        }
        return instance;
    }
    public static void close() {
        if(instance != null) {
            instance = null;
        }
    }
    public void runAllBatch() throws Exception {
    	
    	List<ServiceInfo> infoList = ConfigReader.getInstance().getServerConfig().getServiceInfoList();
    	
        Iterator<ServiceInfo> it = infoList.iterator();
        while(it.hasNext()) {
            ServiceInfo info = it.next();
            if(info.getExecType().equals("batch")) {
            	BatchJob job = new BatchJob(info.getMethod(),
            							    info.getUrl(),
            							    info.getInClass(),
            							    info.getOutClass(),
            							    info.getSleepMSec());
            	job.start();
            	jobList.add(job);
            }
        }
        
        Iterator<BatchJob> it2 = jobList.iterator();
        
        while(it2.hasNext()) {
        	BatchJob curr = it2.next();
        	curr.join();
        }
        
    }
    
}
