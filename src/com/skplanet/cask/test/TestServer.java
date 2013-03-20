package com.skplanet.cask.test;


import com.skplanet.cask.container.ConnectionPoolRegistry;
import com.skplanet.cask.container.Container;
import com.skplanet.cask.container.ServiceRuntimeRegistry;
import com.skplanet.cask.container.config.ConfigReader;


public class TestServer extends Thread {
    public Container container = null; 
    public static boolean isStartedMoreThanOnce = false;
    public boolean fail = false;
    
    public Container getContainer() {
        return container;
    }

    public boolean isLive() {
        if(container != null) {
            return container.isLive();
        }
        return false;
    }
    
    public void startAndWait() throws Exception {
        
        start();
        while(!isLive() && !fail) {
            Thread.sleep(2000); 
        }
    }
    
    public void cleanAll() throws Exception {
        
        ConfigReader.close();
        ConnectionPoolRegistry.close();
        ServiceRuntimeRegistry.close();
    }
    public void setConfigurationFile(String userConfig, String defaultConfig) {
        ConfigReader.setUserConfigFile(userConfig);
        ConfigReader.setDefaultConfigFile(defaultConfig);
    }
    public void setUserConfigFile(String userConfig) {
        ConfigReader.setUserConfigFile(userConfig);
    }
    
    @Override
    public void run()  {
        
        try {
            cleanAll();
            ConfigReader.getInstance().init();
            //ServiceRuntimeRegistry r = ServiceRuntimeRegistry.getInstance();
            container = new Container();
            if(isStartedMoreThanOnce) {
                container.removeMBeans();
            }
            container.start();
            isStartedMoreThanOnce = true;
            
        }catch(Exception e) {
            TestRuntime.printStackTrace(e);
            fail = true;
        }
        
    }
    
    public void end() {
        try {
            container.stop();
            container = null;
            Thread.sleep(1000);
        }catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
