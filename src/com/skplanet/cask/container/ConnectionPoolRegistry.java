package com.skplanet.cask.container;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.skplanet.cask.container.config.DataSourceInfo;


public class ConnectionPoolRegistry {

    private static ConnectionPoolRegistry instance = new ConnectionPoolRegistry();
    private HashMap<String, DBConnectionPool> poolMap = new HashMap<String, DBConnectionPool>();
    


    public static ConnectionPoolRegistry getInstance() {
        if(instance == null) {
            instance = new ConnectionPoolRegistry();
        }
        return instance;
    }

    public static void close() {
        if(instance != null) {
            Iterator<String> it = instance.poolMap.keySet().iterator();
            while(it.hasNext()) {
                String key = it.next();
                DBConnectionPool pool = instance.poolMap.get(key);
                pool.close();
            }
            instance.poolMap = null;
            instance = null;
        }
    }
    
    public void init(List<DataSourceInfo> infoList) {
        
        for(int i = 0; i < infoList.size(); ++i) {
            String id = infoList.get(i).getId();
            DBConnectionPool conn = new DBConnectionPool();
            conn.init(infoList.get(i));
            poolMap.put(id, conn);
        }
    }
    public DBConnectionPool getConnnectionPool(String id) {
        return poolMap.get(id);
    }
    
    public Map<String, DBConnectionPool> getPoolMap() {
        return poolMap;
    }
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        
        Iterator<String> it = poolMap.keySet().iterator();
        
        buf.append("size = ");
        buf.append(poolMap.size());
        buf.append("\n");
        
        while(it.hasNext()) {
            String poolId = it.next();
            buf.append(toString(poolId));
        }
        return buf.toString();
    }
    public String toString(String poolId) {
        StringBuilder buf = new StringBuilder();
        
        DBConnectionPool pool = poolMap.get(poolId);
        buf.append("(");
        buf.append(poolId);
        buf.append(") ");
        buf.append(pool.toString());
        buf.append("\n");
        
        return buf.toString();
    }
}
