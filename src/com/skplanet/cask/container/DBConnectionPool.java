package com.skplanet.cask.container;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.skplanet.cask.container.config.DataSourceInfo;


public class DBConnectionPool {

    private DataSource dataSource = null;
    public void init(DataSourceInfo dataInfo) {
        PoolProperties p = new PoolProperties();
        p.setUrl(dataInfo.getUrl());
        p.setDriverClassName(dataInfo.getDriverClassName());
        p.setUsername(dataInfo.getUsername());
        p.setPassword(dataInfo.getPassword());
        p.setJmxEnabled(true);
        p.setMinIdle(dataInfo.getMinIdle());
        p.setMaxIdle(dataInfo.getMaxIdle());
        p.setMaxActive(dataInfo.getMaxActive());
        p.setInitialSize(dataInfo.getInitialSize());
        p.setDefaultAutoCommit(dataInfo.isDefaultAutoCommit());
        p.setInitSQL(dataInfo.getInitSQL());
        p.setValidationQuery(dataInfo.getValidateQuery());

        dataSource = new DataSource();
        dataSource.setPoolProperties(p);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public void close() {
        dataSource.close();
    }
    public String getUrl() {
        return dataSource.getUrl();
    }
    @Override
    public String toString() {
        return dataSource.getPoolProperties().getUrl() + " : " + 
               dataSource.getPoolProperties().toString();
    }
}
