package com.project1.lawrencedang.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project1.lawrencedang.ProcessInfo;

public class ProcessInfoRepository {
    final String dbPath = "file::memory:?cache=shared";
    Connection putConn;
    Connection getConn;

    public ProcessInfoRepository() throws SQLException
    {
        // Initialize DB
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch(ClassNotFoundException e)
        {
            throw new SQLException("Could not find sqlite driver");
        }
        
        Connection setupConn = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
        PreparedStatement p = setupConn.prepareStatement("CREATE TABLE IF NOT EXISTS Processes ("+
            "id INTEGER PRIMARY KEY,"+
            "name TEXT,"+
            "path TEXT NOT NULL,"+
            "running INTEGER NOT NULL)");
        p.execute();
        
        putConn = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
        getConn = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
        getConn.setAutoCommit(true);
        putConn.setAutoCommit(true);
        setupConn.close();
    }

    public ProcessInfo get(int id) throws SQLException
    {
        PreparedStatement statement = getConn.prepareStatement("SELECT * FROM Processes WHERE id = ?");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        ProcessInfo pi = null;
        while(rs.next())
        {
            // Fill
            int process_id = rs.getInt(1);
            String name = rs.getString(2);
            String path = rs.getString(3);
            boolean running = rs.getBoolean(4);
            pi = new ProcessInfo(process_id, name, path, running);
        }

        return pi;
    }

    public boolean put(ProcessInfo info) throws SQLException
    {
        PreparedStatement statement = putConn.prepareStatement("UPDATE Processes SET running = ? WHERE id = ?");
        statement.setBoolean(1, info.isRunning());
        statement.setInt(2, info.getId());
        return statement.executeUpdate() > 0;
    }

    public int post(ProcessInfo info) throws SQLException
    {
        PreparedStatement statement = putConn.prepareStatement("INSERT INTO Processes(id, name, path, running) VALUES(?, ?, ?, ?)");
        statement.setInt(1, info.getId());
        statement.setString(2, info.getName());
        statement.setString(3, info.getPath());
        statement.setBoolean(4, info.isRunning());
        statement.executeUpdate();
        return info.getId();
    }

    public void shutdown() throws SQLException
    {
        putConn.close();
        getConn.close();
    }

    
}