package com.skplanet.cask.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;

public class DbUtil {
    
    public static void closeAll(Connection conn, PreparedStatement pstmt) throws SQLException {
        try {
            if(pstmt != null) {
                pstmt.close();    
            }
        } finally {
            close(conn);
        }
    }
    public static void closeAll(Connection conn, Statement stmt) throws SQLException {
        try {
            if(stmt != null) {
                stmt.close();    
            }
        } finally {
            close(conn);
        }
    }
    public static void close(Connection conn) throws SQLException {
        if(conn != null) {
            conn.close();
        }
    }    
    public static void rollback(Connection conn, PreparedStatement pstmt) throws SQLException {
        if(pstmt != null) {
            conn.rollback();
        }
    }
    public static void rollback(Connection conn, Statement stmt) throws SQLException {
        if(stmt != null) {
            conn.rollback();
        }
    }
    public static void rsToMap(ResultSet rs, Map<String, Object> resultMap, String idColName) throws SQLException {
        
        int colCount = rs.getMetaData().getColumnCount();
        
        while(rs.next()) {
            Map<String, Object> tempMap = new LinkedHashMap<String, Object>();    
            rsToMapWithIndex(rs, tempMap, 1, colCount);
            resultMap.put(idColName + "-" + 
                          String.valueOf(rs.getInt(idColName)), tempMap);
        }
    }
    public static void rsToMapWithIndex(ResultSet rs, Map<String, Object> tempMap, int startIndex, int lastIndex) throws SQLException {
        
        ResultSetMetaData meta = rs.getMetaData();
        for(int i = startIndex; i <= lastIndex; ++i) {
            String colName = meta.getColumnName(i); 
            if(meta.getColumnClassName(i).equals("java.sql.Timestamp")) {
                tempMap.put(colName, StringUtil.timestamp2Str(rs.getTimestamp(colName)));
            } 
            else {
                tempMap.put(colName, rs.getObject(i));
            }
        }
    }
    public static void rsToMapWithIndex(ResultSet rs, Map<String, Object> tempMap, int startIndex) throws SQLException {
        rsToMapWithIndex(rs, tempMap, startIndex, rs.getMetaData().getColumnCount());
    }
    
    public static <T> void setValue2Pstmt(PreparedStatement pstmt, Logger logger, int index, T value) throws SQLException {
        logger.debug("{} : ({})", index, value);
        pstmt.setObject(index, value);
    }
    
    public static void setNull2Pstmt(PreparedStatement pstmt, Logger logger, int index, int sqlTypes) throws SQLException {
        logger.debug("{} : ({})", index, "null");
        pstmt.setNull(index, sqlTypes);
    }
}
