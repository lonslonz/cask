package com.skplanet.cask.test.testcase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


import com.skplanet.cask.container.Container;
import com.skplanet.cask.test.TestRuntime;
import com.skplanet.cask.test.TestServer;

public class FileTransferTest {
    private Container testContainer = null;

    private static final String POST_MSG = 
            "{ " + 
            "    \"id\" : \"-1\", " +
            "    \"runId\" : \"-1\", " +
            "    \"runLimitSec\" : \"86400\", " +
            "    \"command\" : \"start\", " +
            "    \"params\" :  " +
            "    {   \"source\" : \"%s\", " +
            "        \"dest\" : \"%s\", " +
            "        \"copyType\" : \"normal\" " +
            "    } " +
            "}";
    private static final String FTP_TO_HDFS = 
    "{ " + 
    "    'id' : -1, " +
    "    'runId' : -1, " +
    "    'runLimitSec' : 86400, " +
    "    'command' : 'start', " +
    "    'params' :  " +
    "    {   'source' : 'ftp://lons:dlwhdals@20.20.20.53/ftp_data/smartw/in_org/', " +
    "        'dest' : 'hfs://20.20.20.54:9000/user/hbase/smartw/data/' " +
    "        'copyType' : 'normal' " +
    "    } " +
    "}";
    
    private static String URL = "http://localhost:8080/postino/filetransfer";
    private TestServer server;
    
    
    @Before
    public void start() throws Exception {
        server  = new TestServer();
        server.startAndWait();

    }

    @Test
    public void testFtp() throws IOException, SAXException {
        
        
        Assert.assertNotNull(server.getContainer());
        
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(URL);
 
        String msg = String.format(POST_MSG,
                                   "ftp://lons:dlwhdals@20.20.20.53/ftp_data/smartw/in_org/",
                                   "hdfs://20.20.20.54:9000/user/hbase/smartw/data/");
        
        System.out.println(msg);
        StringEntity input = new StringEntity(msg);
        
        TestRuntime.println("this is diff version");
        TestRuntime.println("this 2");
        
        input.setContentType ("application/json");
        postRequest.setEntity(input);
        
        HttpResponse response = httpClient.execute(postRequest);
 
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        
        BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));
 
        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
 
        httpClient.getConnectionManager().shutdown();
    }

    @After
    public void stop() throws Exception {
        server.end();
    }
}
