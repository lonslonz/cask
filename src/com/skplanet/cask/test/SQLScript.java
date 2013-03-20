package com.skplanet.cask.test;

import java.io.File;
import java.sql.SQLException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

import com.skplanet.cask.container.config.DataSourceInfo;

public class SQLScript extends SQLExec {
    
    private static final String CREATE_DB = "create database if not exists postino_test";
    public void createSchema(String fileName, DataSourceInfo dbInfo, String dbName) throws SQLException {
        
        Project project = new Project();
        project.init();
        
        setUrl(dbInfo.getUrl());
        setUserid(dbInfo.getUsername());
        setPassword(dbInfo.getPassword());
        setAutocommit(true);
        setDriver(dbInfo.getDriverClassName());
       // this.execSQL(CREATE_DB, System.out);
        
        File srcFile = new File(fileName);
        setSrc(srcFile);
        setProject(project);
        setTaskType("sql");
        setTaskName("sql");
        execute();

    }
}
