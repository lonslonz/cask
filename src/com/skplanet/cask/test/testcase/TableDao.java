package com.skplanet.cask.test.testcase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import com.skplanet.cask.container.ConnectionPoolRegistry;
import com.skplanet.cask.container.DBConnectionPool;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.test.TestRuntime;
import com.skplanet.cask.util.DbUtil;

public class TableDao {
    
    private static String CONNECTION_POOL = ConfigReader.getInstance().getServerConfig().getRuntimeDb();
    private static DBConnectionPool pool = ConnectionPoolRegistry.getInstance().getConnnectionPool(CONNECTION_POOL);
    private Connection conn = null;
    
    private static String SELECT_SQL = "select * from %s"; 
    public void selectTable(String tableName) throws Exception  {
        
        conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        
        String query = null;
        try {
                query = String.format(SELECT_SQL, tableName);
                stmt = conn.createStatement();
                
                rs = stmt.executeQuery(query);
                
                ResultSetMetaData meta = rs.getMetaData();
               
                TestRuntime.println("- table : " + tableName);
                TestRuntime.printTable(rs, meta);
                                
                conn.commit();
            } catch(SQLException e) {
                DbUtil.rollback(conn, stmt);
                throw e;  
            } finally {
                if(rs != null) {
                    rs.close();
                }
                DbUtil.closeAll(conn, stmt);
            }
    }
}

