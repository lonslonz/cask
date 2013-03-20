package com.skplanet.cask.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import com.skplanet.cask.container.Container;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.config.DataSourceInfo;
import com.skplanet.cask.test.result.TSuites;
import com.skplanet.cask.util.StringUtil;

public class TestRuntime {
    
    private static TestRuntime instance = new TestRuntime();
    private static final String SERVER_LOG_FILE = "/server.log";
    private static final String RESULT_ORG_DIR = "/result/";
    private static final String RESULT_ORG_FILE = "/result.org";
    private static final String RESULT_NOW_FILE = "/result.now";
    
    private static final String DBTEST_XML = "server_dbtest.xml";
    private static final String SERVER_XML = "server.xml";
    private static final String ORG_XML = "server_org.xml";
    private static final String BACKUP_XML = "server_backup.xml";
    
    private static final String REPORT_XML = "/report.xml";    
    private static final String CREATE_SQL = "/sql/create.sql";
    private static final String RUNTIME_DB = "runtimeDb";
    
    private String testHomePath;
    private String resultFilePath;
    private File resultFile;
    
    private  PrintStream out;
    
    
    public static TestRuntime getInstance() {
        return instance;
    }
    
   
    private static PrintStream out() {
        if(getInstance().out == null) {
            return System.out;
        }
        
        return getInstance().out;
    }
    public String getTestHome() {
        return testHomePath;
    }
    public String getTestConfDir() {
        return testHomePath + "/conf/";
    }
    public void init(String homePath) throws Exception {
        testHomePath = homePath;
        resultFilePath = testHomePath + RESULT_NOW_FILE;
        
        resultFile = new File(this.resultFilePath);
        out = new PrintStream(new FileOutputStream(resultFile));
        
        //System.setOut(out);
        removeOldFiles();
        
    }
    public void changeConfig4DbTest() throws Exception {
        changeConfigFile(getTestConfDir() + DBTEST_XML, getTestConfDir() + SERVER_XML, null);  
    }
    public void changeConfig4Init() throws Exception {
        changeConfigFile(getTestConfDir() + ORG_XML, getTestConfDir() + SERVER_XML, null);  
    }
    
    public void revertConfig() throws Exception  {
        changeConfigFile(getTestConfDir() + BACKUP_XML, getTestConfDir() + ORG_XML, null);
        FileUtils.forceDelete(new File(getTestConfDir() + BACKUP_XML));
    }
    private void changeConfigFile(String src, String dest, String backup) throws Exception {        
        if(backup != null) {
            FileUtils.copyFile(new File(dest),  new File(backup));
        }
        FileUtils.copyFile(new File(src),  new File(dest));
    }
    public void removeOldFiles() {
        File file = new File(testHomePath + SERVER_LOG_FILE);
        if(!file.delete()) {
            System.out.println("Error : fail to delete : " + testHomePath + SERVER_LOG_FILE);
        }
        File orgFile = new File(testHomePath + RESULT_ORG_FILE);
        orgFile.delete();
    }
    
    public boolean assertDiff() throws IOException {
        BufferedReader bufOrg = null;
        BufferedReader bufNow = null;
        FileReader orgReader = null;
        FileReader nowReader = null; 
        try {
            File destFile = new File(testHomePath + RESULT_ORG_FILE);
            File srcFile = new File(testHomePath + RESULT_ORG_DIR + RESULT_ORG_FILE);
            FileUtils.copyFile(srcFile, destFile);
            
            File nowFile = new File(testHomePath + RESULT_NOW_FILE);
            orgReader = new FileReader(destFile);
            nowReader = new FileReader(nowFile);
            
            bufOrg = new BufferedReader(orgReader);
            bufNow = new BufferedReader(nowReader);
    
            String orgStr;
            int lines = 0;
            String nowStr;
            int error = 0;
            while(true) {
                orgStr = bufOrg.readLine();
                nowStr = bufNow.readLine();
                ++lines;
                
                if(orgStr == null || nowStr == null) {
                    if(orgStr != nowStr) {
                        error++;
                    }
                    if(orgStr == null && nowStr == null) {
                        break;
                    } else {
                        continue;
                    }
                    
                }
                
                String regex = "mysql://([^:/]+):";
                String nOrgStr = orgStr.replaceAll(regex, "");
                String nNowStr = nowStr.replaceAll(regex, "");
                
                if(!nOrgStr.equals(nNowStr)) {
                    error++;
                    continue;
                }
            }
            
            if(error > 0) {
                System.out.println("Error : different line : " + lines);    
                return false;
            }
            
            return true;
        } finally {
            if(orgReader != null) {
                orgReader.close();
            }
            if(nowReader != null) {
                nowReader.close();
            }
            if(bufOrg != null) {
                bufOrg.close();
            }
            if(bufNow != null) {
                bufNow.close();
            }
        }
        
        
    }
    
    
    public void close() {
        out.close();
    }
    
    public static void println(String msg) {
        System.out.println(msg);
        out().println(msg);
        out().flush();
    }
    public static void print(String msg) {
        System.out.print(msg);
        out().print(msg);
        out().flush();
    }
    
    public static void printFunction(String name) {
        println("+ " + name);
    }
    public static void printClass(String name) {
        println("++ " + name);
    }
    public static void printSuite(String name) {
        println("+++ " + name);
    }
    public static void  printStackTrace(Exception e) {
        println(StringUtil.exception2Str(e));
    }
    public static void printList(List<?> list) {
        
        Iterator<?> it = list.iterator();
        int count = 1;
        int size = list.size();
        while(it.hasNext()) {
            println(String.format("(%d/%d) %s", count, size, it.next()));
            ++count;
        }
    }
    public static void printMap(Map<String, ?> map) {
        Iterator<String> it = map.keySet().iterator();
        
        int size = map.size();
        int count = 1;
        while(it.hasNext()) {
            String key = it.next();
            println(String.format("(%d/%d) %s = %s", count, size, key, map.get(key)));
            ++count;
        }
    }
    @SuppressWarnings("unchecked")
    public static void printNestedMap(Map<String, ?> map, String[] ignoreSuffix) {
        
        
        Iterator<String> it = map.keySet().iterator();
        
        int size = map.size();
        int count = 1;
        
        while(it.hasNext()) {
            String key = it.next();
            
            Map<String, ?> innerMap = (Map<String, ?>)map.get(key);
            
            Iterator<String> innerIt = innerMap.keySet().iterator();

            while(innerIt.hasNext()) {
                String innerKey = innerIt.next();

                if(!containIgnore(innerKey, ignoreSuffix)) {
                    println(String.format("(%d/%d)  %s = %s", 
                            count, size, innerKey, innerMap.get(innerKey)));
                }
            }
            ++count;
        }
    }
    private static boolean containIgnore(String text, String[] ignore) {
        for(int i = 0; i < ignore.length; i++) {
            if(text.contains(ignore[i])) {
               return true;
            }
        }
        return false;
    }
    public static void printContainerServices(TestServer server) {
        Map<String, Object> beanMap = server.getContainer().getServices();
        
        Iterator<String> iter = beanMap.keySet().iterator();
        
        int count = 1;
        int size = beanMap.size();
        while(iter.hasNext()) {
            String beanName = iter.next();
            if(beanName.startsWith("/") || beanName.contains("skplanet")) {
                Object obj = beanMap.get(beanName);
                println(String.format("(%d/%d) %s => %s", count, size, beanName, obj.getClass().getName()));
                ++count;
            }
        }
    }
    public static void printContainerMBeans() throws Exception {
        ArrayList<MBeanServer> mServerArray = MBeanServerFactory
                .findMBeanServer(null);
        MBeanServer mBeanServer = mServerArray.get(0);
        String pattern = String.format(
                "%s:%s=*",
                Container.getMBeanDomain(), Container.getMBeanKey());
        
        ObjectName name = new ObjectName(pattern);
        Set<ObjectName> names = mBeanServer.queryNames(name, null);
        Iterator<ObjectName> it = names.iterator();
        
        int count = 1;
        int size = names.size();
        while(it.hasNext()) {
            ObjectName obj = it.next();
            println(String.format("(%d/%d) %s", count, size, obj.toString()));
            ++count;
        }
    }
    
    public static void printTable(ResultSet rs, ResultSetMetaData meta) throws SQLException {
        
        int size = meta.getColumnCount();
        
        StringBuilder buf = new StringBuilder();
        int count = 1;

        while(rs.next()) {
            
            buf.append(String.format("(%d) ", count));
            for(int i = 1; i <= size; ++i) {
                if(meta.getColumnTypeName(i).toUpperCase().equals("DATETIME") || 
                        meta.getColumnName(i).toUpperCase().equals("VERSION") ||
                        meta.getColumnName(i).toUpperCase().equals("SERVER_HOME")) {
                    continue;
                }
                
                buf.append(String.format("(%s = %s) ",  meta.getColumnName(i),rs.getObject(i)));
            }
            count++;
            buf.append("\n");
        }
        print(buf.toString());
    }
    
    
    public void setupFtpEnv() {
        
    }
    
    // drop & create db
    // create table
    
    public void setupDbEnv() throws Exception {
        
        ConfigReader.getInstance().init();
        List<DataSourceInfo> poolList = ConfigReader.getInstance().getServerConfig().getDataSourceInfoList();
        
        Iterator<DataSourceInfo> it = poolList.iterator();
        DataSourceInfo info = null;
        
        while(it.hasNext()) {
            info = it.next();
            if(info.getId().equals(ConfigReader.getInstance().getServerConfig().getPropValue(RUNTIME_DB))) {
                break;
                
            }
        }
        
        URI jdbc = new URI(info.getUrl().substring(5));
        String dbName = jdbc.getPath().substring(1);
        
        SQLScript script = new SQLScript();
        script.createSchema(testHomePath + CREATE_SQL,  
                            info,
                            dbName);
    }

    public void writeReport(TSuites suites) throws Exception {
        //InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(TSuites.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Writer w = null;
        File file = new File(testHomePath + REPORT_XML);
        try {
            w = new FileWriter(file);
            marshaller.marshal(suites, w);
        } finally {
            if(w != null) {
                w.close();
            }
        }
    }
}
