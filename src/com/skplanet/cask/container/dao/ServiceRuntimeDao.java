package com.skplanet.cask.container.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ConnectionPoolRegistry;
import com.skplanet.cask.container.DBConnectionPool;
import com.skplanet.cask.container.ServerStatus;
import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.ServiceRuntimeRegistry;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.config.ServiceInfo;
import com.skplanet.cask.util.DbUtil;

public class ServiceRuntimeDao {

    private static final Logger logger = LoggerFactory
            .getLogger(ServiceRuntimeDao.class);
    
    private static final String CONNECTION_POOL = ConfigReader.getInstance().getServerConfig().getRuntimeDb();
    private static DBConnectionPool pool = ConnectionPoolRegistry.getInstance().getConnnectionPool(CONNECTION_POOL);
    
    private static String INSERT_SERVICE_SQL = 
            "insert into tb_service_runtime   " +
            "(server_runtime_id, url, service_type,  service_class, mbean_class, exec_type, status,  reg_time, update_time) values " +
            "(?, ?, ?, ?, ?, ?, 'open', now(), now())";
        
    private static String SELECT_SERVER_SQL = 
            "select SE.service_runtime_id, SE.url, SE.service_type, SE.service_class, SE.mbean_class, SE.exec_type, SE.status, SE.reg_time, SE.update_time, S.server_runtime_id, S.server, S.port, S.status as server_status " +
            "from tb_server_runtime S, tb_service_runtime SE " + 
            "where S.server_runtime_id = SE.server_runtime_id %s " +
            "order by SE.service_runtime_id desc";

    private Connection conn = null;
    private String makeQuery(String server, String status, String id, String url, String serverId) throws Exception {
        
        String query = "";
        
        if(id != null) {
            query += "and SE.service_runtime_id = ? ";
            return query;
        }

        if(server != null) { 
            query += "and S.server = ? ";
        }
        
        if(url != null) {
           query += "and SE.url = ? "; 
        }
        if(serverId != null) {
            query += "and S.server_runtime_id = ? ";
        }
        
        if(status != null && id == null && !status.equals(ServerStatus.ALL.getLowerStr())) {
            if(status.equals(ServerStatus.RUNNING.getLowerStr())) {
                query += "and S.end_time is null ";    
            }
            else {
                query += "and S.status = '" + status.toLowerCase() + "' ";
            }
        }
        return query;
    }
    public Map<String, Object> select(String server, String status, String id, String url, String serverId) throws Exception {
        
        conn = pool.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String query = null;
        try {
            
            query = makeQuery(server, status, id, url, serverId);
            query = String.format(SELECT_SERVER_SQL, query);
            logger.debug("Query : " + query);
            pstmt = conn.prepareStatement(query);
            
            int index = 1;
            if(id != null) {
                DbUtil.setValue2Pstmt(pstmt, logger, index++, Integer.parseInt(id));
            }
            if(server != null) {
                DbUtil.setValue2Pstmt(pstmt, logger, index++, server);
            }
            
            if(url != null) {
                DbUtil.setValue2Pstmt(pstmt, logger, index++, url);
            }
            
            if(serverId != null) {
                DbUtil.setValue2Pstmt(pstmt, logger, index++, serverId);
            }
            
            rs = pstmt.executeQuery();
            
            
            DbUtil.rsToMap(rs, resultMap, "service_runtime_id");
            
            conn.commit();
        } catch(SQLException e) {
            DbUtil.rollback(conn, pstmt);
            logger.error("Query : " + query);
            throw e;  
        } finally {
            
            if(rs != null) {
                rs.close();
            }
            DbUtil.closeAll(conn, pstmt);
        }
        
        return resultMap;
    }
    
    // 1. insert runtime service info into db.
    // 2. set db's auto column id to registry.
    public void insert(int serverId, 
                      List<ServiceInfo> serviceList, 
                      ServiceRuntimeRegistry runtimeRegistry) throws Exception {
        
        conn = pool.getConnection();
        logger.debug("Query : " + INSERT_SERVICE_SQL);
        PreparedStatement pstmt = conn.prepareStatement(INSERT_SERVICE_SQL, 
                                                        Statement.RETURN_GENERATED_KEYS);
        ResultSet resultSet = null;
        
        try {
            for(int i = 0; i < serviceList.size(); i++) {
                ServiceInfo info = serviceList.get(i);
                
                DbUtil.setValue2Pstmt(pstmt, logger, 1, serverId);
                DbUtil.setValue2Pstmt(pstmt, logger, 2, info.getUrl());
                DbUtil.setValue2Pstmt(pstmt, logger, 3, "not supported");
                DbUtil.setValue2Pstmt(pstmt, logger, 4, info.getClassName());
                
                
                if(info.getMBeanClassName() == null) {
                    DbUtil.setNull2Pstmt(pstmt, logger, 5, Types.VARCHAR);
                    
                } else {
                    DbUtil.setValue2Pstmt(pstmt, logger, 5, info.getMBeanClassName());
                }
                DbUtil.setValue2Pstmt(pstmt, logger, 6, info.getExecType());
                
                pstmt.executeUpdate();
                
                int newId;
                resultSet = pstmt.getGeneratedKeys();
                
                if(resultSet.next()) {
                    newId = resultSet.getInt(1);
                } else {
                    throw new SQLException("auto id is not retievied.");
                }
                
                ServiceRuntimeInfo runInfo = runtimeRegistry.getServiceRuntimeInfo(info.getUrl());
                runInfo.setServiceRuntimeId(newId);
            }
            conn.commit();
            
        } catch(Exception e) {
            DbUtil.rollback(conn, pstmt);
	        logger.error("Query : " + INSERT_SERVICE_SQL);
	        throw e;
        } finally {
            DbUtil.closeAll(conn, pstmt);
        }
        
    }

    /*
    private void convertRsToMap(ResultSet rs, Map<String, Object> resultMap) throws SQLException {
        
        while(rs.next()) {
            Map<String, Object> tempMap = new LinkedHashMap<String, Object>();
            
            tempMap.put("service_runtime_id", rs.getInt("service_runtime_id"));
            tempMap.put("url", rs.getString("url"));
            tempMap.put("service_type", rs.getString("service_type"));
            tempMap.put("service_class", rs.getString("service_class"));
            tempMap.put("mbean_class", rs.getString("mbean_class"));
            tempMap.put("exec_type", rs.getString("exec_type"));
            tempMap.put("status", rs.getString("status"));
            tempMap.put("reg_time", StringUtil.timestampToStr(rs.getTimestamp("reg_time")));
            tempMap.put("update_time", StringUtil.timestampToStr(rs.getTimestamp("update_time")));
            tempMap.put("server_runtime_id", rs.getInt("server_runtime_id"));
            tempMap.put("server", rs.getString("server"));
            tempMap.put("port", rs.getInt("port"));
            tempMap.put("server_status", rs.getString("server_status"));
         
            resultMap.put("service_runtime_id-" + String.valueOf(rs.getString("service_runtime_id")), (Object)tempMap);
        }
    }
    */
}
