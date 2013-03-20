package com.skplanet.cask.test.testcase;

import java.io.IOException;
import java.util.List;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.config.ServerConfig;
import com.skplanet.cask.container.config.ServiceInfo;
import com.skplanet.cask.test.TestRuntime;
import com.skplanet.cask.test.TestUtil;

public class ConfigReaderTest {
    
    public String resourceHome; // = "com.skplanet.postino.test.testcase.configTest.";
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup() throws IOException {
        TestRuntime.printClass(ConfigReaderTest.class.getName());
        
        resourceHome = TestUtil.getPackagePath(ConfigReaderTest.class);
    }
    
    @Test
    public void testNothingDefault() {
        
        TestRuntime.printFunction(testName.getMethodName());
        
        try {
            TestRuntime.println("- read xml from : " + resourceHome);
            TestRuntime.println("user : " +resourceHome + "/nothing_configreader.xml");
            TestRuntime.println("default : " + resourceHome + "/default_configreader.xml");
            
            ConfigReader.getInstance().init(resourceHome + "/nothing_configreader.xml", 
                                            resourceHome + "/default_configreader.xml");
            ServerConfig serverConfig = ConfigReader.getInstance().getServerConfig();
            TestRuntime.println("- server info : ");
            TestRuntime.println(serverConfig.getServerInfo().toString());
            
            List<ServiceInfo> list = serverConfig.getServiceInfoList();
            TestRuntime.println("- service info list : ");
            TestRuntime.printList(list);
            
        } catch(Exception e) {
            TestRuntime.printStackTrace(e);
        }
    }
    @Test
    public void testUserDefault() {
        TestRuntime.printFunction(testName.getMethodName());
        
        try {
            TestRuntime.println("- read xml from : " + resourceHome);
            TestRuntime.println("user : " + resourceHome + "/cask_configreader.xml");
            TestRuntime.println("default : " + resourceHome + "/default_configreader.xml");
            
            ConfigReader.getInstance().init(resourceHome + "/cask_configreader.xml", 
                                            resourceHome + "/default_configreader.xml");
            ServerConfig serverConfig = ConfigReader.getInstance().getServerConfig();
            
            TestRuntime.println("- server info : ");
            TestRuntime.println(serverConfig.getServerInfo().toString());
            
            List<ServiceInfo> list = serverConfig.getServiceInfoList();
            TestRuntime.println("- service info list : ");
            TestRuntime.printList(list);
            
        } catch(Exception e) {
            TestRuntime.printStackTrace(e);
        }
    }
    
    
}
