package com.skplanet.cask.test.testcase;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.skplanet.cask.container.ConnectionPoolRegistry;
import com.skplanet.cask.container.Container;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.config.ServerConfig;
import com.skplanet.cask.test.TestRuntime;
import com.skplanet.cask.test.TestServer;

public class ContainerTest {
    @Rule
    public static TestName testName = new TestName();
    private static TestServer server = null;
    
    @Before
    public void before() throws Exception {
        TestRuntime.printClass(ContainerTest.class.getName());
        server = new TestServer();
        server.startAndWait();
    }
    @After
    public void after() throws Exception {
        server.end();
    }

    @Test
    public void testStartupShutdown() throws Exception {
        TestRuntime.printFunction(testName.getMethodName());
        
        ServerConfig serverConfig = ConfigReader.getInstance().getServerConfig();
        TestRuntime.println("- server info : ");
        TestRuntime.println(serverConfig.getServerInfo().toString());
    }
    
    @Test
    public void testGetServices() throws Exception {
        TestRuntime.printFunction(testName.getMethodName());
        
        ServerConfig serverConfig = ConfigReader.getInstance().getServerConfig();
        TestRuntime.println("- configured server info : ");
        TestRuntime.printList(serverConfig.getServiceInfoList());
        
        TestRuntime.println("- registered server info : ");
        TestRuntime.printContainerServices(server);        
        
    }
    @Test 
    public void testAddService() throws Exception {
        TestRuntime.printFunction(testName.getMethodName());
        
        
        server.getContainer().addService(
                "/addtestsimple", 
                "com.skplanet.cask.container.service.SimpleServiceAdapter",
                AddTestSimpleService.class.getName(),
                AddTestSimpleService.class.getName());
           
        TestRuntime.println("- registered server info : insert /addtestsimple");
        TestRuntime.printContainerServices(server);
    }
    @Test
    public void testAddMBean() throws Exception {
        TestRuntime.printFunction(testName.getMethodName());
        
        TestRuntime.println("- before add mbean");
        TestRuntime.printContainerMBeans();
        
        String url = "/addtestmbean";
        server.getContainer().addService(
                url, 
                "com.skplanet.cask.container.service.SimpleServiceAdapter",
                AddTestSimpleService.class.getName(),
                AddTestSimpleService.class.getName());
        server.getContainer().addMBean(
                Container.getMBeanDomain(), 
                Container.getMBeanKey(),
                url,
                AddTestMonitor.class.getName());
        
        TestRuntime.println("- after add mbean");
        TestRuntime.printContainerMBeans();
    }
    @Test
    public void testDbConnection() {
        TestRuntime.printFunction(testName.getMethodName());
        
        TestRuntime.println("- store db");
        TestRuntime.println("" + ConfigReader.getInstance().getServerConfig().saveRuntimeIntoDb());
        
        TestRuntime.println("- connection pools");
        TestRuntime.println(ConnectionPoolRegistry.getInstance().toString());
        
        TestRuntime.println("- connection pools : " + ConfigReader.getInstance().getServerConfig().getRuntimeDb());
        TestRuntime.println(ConnectionPoolRegistry.getInstance().toString(
                ConfigReader.getInstance().getServerConfig().getRuntimeDb()));
    }
    @Test
    public void testProperty() {
        TestRuntime.printFunction(testName.getMethodName());
        TestRuntime.println("- property");
        
        ConfigReader.getInstance().getServerConfig().getProperties();
        TestRuntime.printList(ConfigReader.getInstance().getServerConfig().getProperties());
    }
}
