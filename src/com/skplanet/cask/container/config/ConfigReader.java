package com.skplanet.cask.container.config;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;



public class ConfigReader {

    private static ConfigReader instance = new ConfigReader();
    
    private static String DEFAULT_CONFIG_FILE = "default.xml";
    private static String USER_CONFIG_FILE = "server.xml";
    private static String VERSION_CONFIG_FILE = "version.properties";

 //   private ServerConfig userConfig;
    private ServerConfig defaultConfig;
    private ServerConfig serverConfig;
    private Configuration systemConfig;
    private Configuration versionConfig;

    public static ConfigReader getInstance() {
        if(instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public static void close() {
        instance = null;
    }
    
    public Configuration getSystemConfig() {
        return getInstance().systemConfig;
    }
//    public ServerConfig getUserConfig() {
//        return getInstance().userConfig;
//    }

    public ServerConfig getServerConfig() {
        return getInstance().serverConfig;
    }
    
    public static void setDefaultConfigFile(String configFile) {
        DEFAULT_CONFIG_FILE = configFile;
    }
    public static void setUserConfigFile(String configFile) {
        USER_CONFIG_FILE = configFile;
    }
    public static String getUserConfigFile() {
        return USER_CONFIG_FILE;
    }
    
    public String getVersion() {
        return getInstance().versionConfig.getString("VERSION");
    }
    public String getHome() {
        return getInstance().systemConfig.getString("server.home") + "/";
    }
    
    public void init() throws Exception {
        init(VERSION_CONFIG_FILE, DEFAULT_CONFIG_FILE, USER_CONFIG_FILE);
    }
    public void init(String userConfig, String defaultConfig) throws Exception {
        init(VERSION_CONFIG_FILE, defaultConfig, userConfig);
    }
    public void init(String userConfig) throws Exception {
        init(VERSION_CONFIG_FILE, DEFAULT_CONFIG_FILE, userConfig);
    }
    
    public void init(String versionFile, String defaultFile, String userFile) throws Exception {
        
        versionConfig = new PropertiesConfiguration(
                ConfigurationUtils.locate(versionFile));
        systemConfig = new SystemConfiguration();
        
        defaultConfig = parseConfig(defaultFile);
        serverConfig = parseConfig(userFile);
        mergeConfig(serverConfig, defaultConfig);
        
        serverConfig.makePropMap();
    }

    private void mergeConfig(ServerConfig userConfig, ServerConfig defaultConfig) {

        if(userConfig.getServerInfo() == null) {
            userConfig.setServerInfo(new ServerInfo());
        }
        ServerInfo.copyIfNull(defaultConfig.getServerInfo(), userConfig.getServerInfo());
        
        if(userConfig.getServiceInfoList() == null) {
            userConfig.setServiceInfoList(new ArrayList<ServiceInfo>());
        }
        userConfig.getServiceInfoList().addAll(defaultConfig.getServiceInfoList());
    }
    
    private ServerConfig parseConfig(String file) throws JAXBException {
        
        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(ServerConfig.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ServerConfig config = (ServerConfig)jaxbUnmarshaller.unmarshal(is);
        return config;
    }
    
    public void saveConfig(String file) throws Exception {
        
        //InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(ServerConfig.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Writer w = null;
        try {
            w = new FileWriter(file);
            marshaller.marshal(serverConfig, w);
        } finally {
            if(w != null) {
                w.close();
            }
        }
    }
 
        /*
                    private XMLConfiguration userConfig = new XMLConfiguration ();
    private XMLConfiguration defaultConfig = new XMLConfiguration ();
    
    private CombinedConfiguration serverConfig = null; 


        XMLConfiguration userConfig = new XMLConfiguration(ConfigurationUtils.locate(USER_CONFIG_FILE));
        XMLConfiguration defaultConfig = new XMLConfiguration(ConfigurationUtils.locate(DEFAULT_CONFIG_FILE));
        
        NodeCombiner combine = new UnionCombiner();
        combine.addListNode("services.service");
        
        serverConfig = new CombinedConfiguration(combine);
        serverConfig.addConfiguration(userConfig);
        serverConfig.addConfiguration(defaultConfig);
        
        System.out.println("" + serverConfig.getString("server(0)[@name]") + "," +
                serverConfig.getString("server(0)[@port]"));
        System.out.println("" + serverConfig.getString("server(1)[@name]") + "," +
                serverConfig.getString("server(1)[@port]"));
        
        List<Object> aaa = serverConfig.getList("services.service.[@url]");
        System.out.println(aaa.size());;
        System.out.println("" + serverConfig.getString("services.service(0)[@url]") + "," );
        System.out.println("" + serverConfig.getString("services.service(1)[@url]") + "," );
        System.out.println("" + serverConfig.getString("services.service(2)[@url]") + "," );
        System.out.println("" + serverConfig.getString("services.service(0)[@type]") + "," );
        System.out.println("" + serverConfig.getString("services.service(1)[@type]") + "," );
  
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                DEFAULT_CONFIG_FILE2);
        Map<String, Object> json = mapper.readValue(is, Map.class);
        //JsonNode json2 = mapper.readValue(is, JsonNode.class);
        System.out.println("" + json);
        //System.out.println("" + json2);
*/  
}
