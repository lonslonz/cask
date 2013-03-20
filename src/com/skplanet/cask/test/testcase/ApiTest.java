package com.skplanet.cask.test.testcase;



import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.test.TestRuntime;
import com.skplanet.cask.test.TestServer;
import com.skplanet.cask.util.HttpUtil;

public class ApiTest {
    @Rule
    public static TestName testName = new TestName();
    private static TestServer server = null;
    
    
    
    @Before
    public void before() throws Exception {
        TestRuntime.printClass(ApiTest.class.getName());
        
        server = new TestServer();
        server.startAndWait();
        
    }
    @After
    public void after() throws Exception {
        server.end();
    }

    @Test
    public void testDb() throws Exception {
        TestRuntime.printFunction(testName.getMethodName());
    
        TestRuntime.println("- store db");
        TestRuntime.println("" + ConfigReader.getInstance().getServerConfig().saveRuntimeIntoDb());
        
        TableDao dao = new TableDao();
        dao.selectTable("tb_server_runtime");
        dao.selectTable("tb_service_runtime");
    }
    
    
    @Test 
    public void testServer() throws Exception {
        
        TestRuntime.printFunction(testName.getMethodName());
    
        String cond = "";
        sendHttpGetServer(cond);
        
        cond = "?status=running";
        sendHttpGetServer(cond);
        
        cond = "?status=end";
        sendHttpGetServer(cond);
        
        cond = "?status=all";
        sendHttpGetServer(cond);
        
        cond = "?status=abnormal";
        sendHttpGetServer(cond);
        
        cond = "?server=localhost";
        sendHttpGetServer(cond);
        
        cond = "?server=nothing";
        sendHttpGetServer(cond);
        
        cond = "?server=localhost&status=end";
        sendHttpGetServer(cond);

        cond = "?server=localhost&status=running";
        Map<String, Object> resultMap = sendHttpGetServer(cond);
     
        cond = "?id=" + getIdFromResult(resultMap, "server_runtime_id");
        sendHttpGetServer(cond);
    }
    @Test
    public void testService() throws Exception {
        TestRuntime.printFunction(testName.getMethodName());
        
        String cond = "";
        sendHttpGetService(cond);
        
        cond = "?status=running";
        sendHttpGetService(cond);
        
        cond = "?status=end";
        sendHttpGetService(cond);
        
        cond = "?status=all";
        sendHttpGetService(cond);
        
        cond = "?status=abnormal";
        sendHttpGetService(cond);
        
        cond = "?server=localhost";
        sendHttpGetService(cond);
        
        cond = "?server=nothing";
        sendHttpGetService(cond);
        
        cond = "?server=localhost&status=end";
        sendHttpGetService(cond);

        cond = "?server=localhost&status=running";
        Map<String, Object> resultMap = sendHttpGetService(cond);
     
        cond = "?id=" + getIdFromResult(resultMap, "service_runtime_id");
        sendHttpGetService(cond);
        
    }
    private Map<String, Object> sendHttpGetService(String cond) throws Exception {
        String[] ignoreSuffix = {"_time", "server_home", "version"};
        String urlPrefix = ConfigReader.getInstance().getServerConfig().getServerInfo().getHttpServerAddr() + 
                           "/servicelist";
        
        TestRuntime.println("- get service info : " + cond);
        Map<String, Object> resultMap = HttpUtil.sendHttpGet2Map(urlPrefix + cond);
        TestRuntime.printNestedMap(resultMap, ignoreSuffix);
        return resultMap;
    }
    
    private Map<String, Object> sendHttpGetServer(String cond) throws Exception {
        String[] ignoreSuffix = {"_time", "server_home", "version"};
        String urlPrefix = ConfigReader.getInstance().getServerConfig().getServerInfo().getHttpServerAddr() +
                           "/processlist";
        
        TestRuntime.println("- get server info : " + cond);
        Map<String, Object> resultMap = HttpUtil.sendHttpGet2Map(urlPrefix + cond);
        TestRuntime.printNestedMap(resultMap, ignoreSuffix);
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    private Object getIdFromResult(Map<String, Object> resultMap, String idName) {
        
        Iterator<String> it = resultMap.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            Map<String, Object> innerMap = (Map<String, Object>)resultMap.get(key);
            return innerMap.get(idName);
        }
        return null;
    }

}
