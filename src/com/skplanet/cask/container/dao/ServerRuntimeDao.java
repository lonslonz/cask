package com.skplanet.cask.container.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ConnectionPoolRegistry;
import com.skplanet.cask.container.DBConnectionPool;
import com.skplanet.cask.container.ServerStatus;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.util.DbUtil;

public class ServerRuntimeDao {

    private static final Logger logger = LoggerFactory
            .getLogger(ServerRuntimeDao.class);
    
    
    private static String INSERT_SERVER_SQL =
                    "insert into tb_server_runtime (server, port, server_home, version, begin_time, end_time, status, update_time ) values " +
                    "(?, ?, ?, ?, now(), NULL, ?, now())";
    
    private static String END_SERVER_SQL =
            "update tb_server_runtime set end_time = now(), update_time = now(), status = ? where server_runtime_id = ?";
    
    private static String UPDATE_ABNORMAL_SQL =
            "update tb_server_runtime set end_time = now(), update_time = now(), status = ? " +
    "where server = ? and port = ? and end_time is null ";
    
    private static String SELECT_SERVER_SQL = 
            "select server_runtime_id, server, port, server_home, version, status, begin_time, end_time, update_time " +
            "from tb_server_runtime where 1=1 %s order by end_time desc";    
    
    private static String CONNECTION_POOL = ConfigReader.getInstance().getServerConfig().getRuntimeDb();
    private static DBConnectionPool pool = ConnectionPoolRegistry.getInstance().getConnnectionPool(CONNECTION_POOL);
    private Connection conn = null;
    
    public int updateAbnormal(String server, int port)  throws SQLException {
        conn = pool.getConnection();
        PreparedStatement pstmt = null;
        int affected = -1;
        
        
        try {
            logger.debug("Query : " + UPDATE_ABNORMAL_SQL);
            pstmt = conn.prepareStatement(UPDATE_ABNORMAL_SQL);
            
            DbUtil.setValue2Pstmt(pstmt, logger, 1, ServerStatus.ABNORMAL.getLowerStr());
            DbUtil.setValue2Pstmt(pstmt, logger, 2, server);
            DbUtil.setValue2Pstmt(pstmt, logger, 3, port);
            
            affected = pstmt.executeUpdate();
            conn.commit();
        
        } catch(SQLException e) {
            DbUtil.rollback(conn, pstmt);
            logger.error("Query : " + END_SERVER_SQL);
            throw e;  
        } finally {
            DbUtil.closeAll(conn, pstmt);
        }
        return affected;
    } 
    public int updateEnd(int serverRuntimeId)  throws SQLException {
        conn = pool.getConnection();
        PreparedStatement pstmt = null;
        int affected = -1;
        try {
            
            logger.debug("Query : " + END_SERVER_SQL);
            pstmt = conn.prepareStatement(END_SERVER_SQL);
            
            DbUtil.setValue2Pstmt(pstmt, logger, 1, ServerStatus.END.getLowerStr());
            DbUtil.setValue2Pstmt(pstmt, logger, 2, serverRuntimeId);
            
            affected = pstmt.executeUpdate();
            conn.commit();
        
        } catch(SQLException e) {
            DbUtil.rollback(conn, pstmt);
        	logger.error("Query : " + END_SERVER_SQL);
        	throw e;  
        } finally {
        	DbUtil.closeAll(conn, pstmt);
        }
        return affected;
    } 
    public int insert(String server, int port, String serverHome, String version)  throws SQLException {
        conn = pool.getConnection();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int affected = -1;
        int newId = -1;

        try {
            logger.debug("Query : " + INSERT_SERVER_SQL);
            pstmt = conn.prepareStatement(INSERT_SERVER_SQL, Statement.RETURN_GENERATED_KEYS);
            
            DbUtil.setValue2Pstmt(pstmt, logger, 1, server);
            DbUtil.setValue2Pstmt(pstmt, logger, 2, port);
            DbUtil.setValue2Pstmt(pstmt, logger, 3, serverHome);
            DbUtil.setValue2Pstmt(pstmt, logger, 4, version);
            DbUtil.setValue2Pstmt(pstmt, logger, 5, ServerStatus.RUNNING.getLowerStr());
            
            affected = pstmt.executeUpdate();
            if(affected <= 0) { 
                throw new SQLException("insert failed");
            }
        
            resultSet = pstmt.getGeneratedKeys();
            if(resultSet.next()) {
                newId = resultSet.getInt(1);
            } else {
                throw new SQLException("auto id is not retievied.");

            }
            conn.commit();
        
        } catch(SQLException e) {
            DbUtil.rollback(conn, pstmt);
            logger.error("Query : " + INSERT_SERVER_SQL);
            throw e;  
        } finally {
            DbUtil.closeAll(conn, pstmt);
        }
        return newId;
    } 
    private String makeQuery(String server, String status, String id) {
        
        
        boolean pre = false;
        String query = "";
        
        if(id != null) {
            query += "and server_runtime_id = ? ";
            return query;
        }
        
        if(server != null) {
            query += "and server = ? ";
            pre = true;
        }
        
        if(status != null && id == null && !status.equals(ServerStatus.ALL.getLowerStr())) {
            
            if(status.equals(ServerStatus.RUNNING.getLowerStr())) {
                query += "and end_time is null ";    
            }
            else {
                query += "and status = '" + status.toLowerCase() + "' ";
            }
        }
        return query;
    }
    public Map<String, Object> select(String server, String status, String id) throws Exception {
    
        conn = pool.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String query = null;
        try {
            
            query = makeQuery(server, status, id);
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
            
            rs = pstmt.executeQuery();
            DbUtil.rsToMap(rs, resultMap, "server_runtime_id");
            
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

    /*
    private void convertRsToMap(ResultSet rs, Map<String, Object> resultMap) throws SQLException {
        
        while(rs.next()) {
            Map<String, Object> tempMap = new LinkedHashMap<String, Object>();
            
            tempMap.put("server_runtime_id", rs.getInt("server_runtime_id"));
            tempMap.put("server", rs.getString("server"));
            tempMap.put("port", rs.getInt("port"));
            tempMap.put("server_home", rs.getString("server_home"));
            tempMap.put("version", rs.getString("version"));
            tempMap.put("status", rs.getString("status"));
            tempMap.put("begin_time", rs.getTimestamp("begin_time"));
            tempMap.put("end_time", rs.getTimestamp("end_time"));
            tempMap.put("update_time", rs.getTimestamp("update_time"));
            
            tempMap.put("begin_time", StringUtil.timestampToStr(rs.getTimestamp("begin_time")));
            tempMap.put("end_time", StringUtil.timestampToStr(rs.getTimestamp("end_time")));
            tempMap.put("update_time", StringUtil.timestampToStr(rs.getTimestamp("update_time")));

            resultMap.put("server_runtime_id-" + String.valueOf(rs.getInt("server_runtime_id")), (Object)tempMap);
        }
    }
    */
   
}
