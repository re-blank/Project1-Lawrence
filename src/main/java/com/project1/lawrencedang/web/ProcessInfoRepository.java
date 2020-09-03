package com.project1.lawrencedang.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project1.lawrencedang.ProcessInfo;
import com.zaxxer.hikari.HikariDataSource;

/**
 * A DAO that manages connections to an in-memory SQLite database. Because the entries are reloaded on
 * startup, there is no need to persist the database to a file.
 */
public class ProcessInfoRepository {
    final String dbPath = "file::memory:?cache=shared";

    HikariDataSource ds;

    /**
     * Initializes a new ProcessInfo repository.
     * @throws SQLException if a connection cannot be established to the in-memory database.
     */
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
        ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:sqlite:"+dbPath);
        Connection setupConn = ds.getConnection();
        PreparedStatement p = setupConn.prepareStatement("CREATE TABLE IF NOT EXISTS Processes ("+
            "id INTEGER PRIMARY KEY,"+
            "name TEXT,"+
            "path TEXT NOT NULL,"+
            "running INTEGER NOT NULL)");
        p.execute();
        
        setupConn.close();
    }

    /**
     * Gets the ProcessInfo with the specified id.
     * @throws SQLException
     */
    public ProcessInfo get(int id) throws SQLException
    {
        try(Connection getConn = ds.getConnection())
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
    }

    /**
     * Gets all Processes entries as ProcessInfo.
     * @throws SQLException
     */
    public List<ProcessInfo> get() throws SQLException
    {
        try(Connection getConn = ds.getConnection())
        {
            PreparedStatement statement = getConn.prepareStatement("SELECT * FROM Processes ORDER BY id ASC");
            ResultSet rs = statement.executeQuery();
            ProcessInfo pi = null;
            List<ProcessInfo> piList = new ArrayList<>();
            while(rs.next())
            {
                // Fill
                int process_id = rs.getInt(1);
                String name = rs.getString(2);
                String path = rs.getString(3);
                boolean running = rs.getBoolean(4);
                pi = new ProcessInfo(process_id, name, path, running);
                piList.add(pi);
            }

            return piList;
        }   
    }

    /**
     * Updates the Processes entry corresponding to the specified ProcessInfo with the new running state.
     * @throws SQLException
     */
    public boolean put(ProcessInfo info) throws SQLException
    {
        try(Connection putConn = ds.getConnection())
        {
            PreparedStatement statement = putConn.prepareStatement("UPDATE Processes SET running = ? WHERE id = ?");
            statement.setBoolean(1, info.isRunning());
            statement.setInt(2, info.getId());
            return statement.executeUpdate() > 0;
        }
        
    }

    /**
     * Creates a new Processes entry reflecting the supplied ProcessInfo
     * @throws SQLException
     */
    public int post(ProcessInfo info) throws SQLException
    {
        try(Connection putConn = ds.getConnection())
        {
            PreparedStatement statement = putConn.prepareStatement("INSERT INTO Processes(id, name, path, running) VALUES(?, ?, ?, ?)");
            statement.setInt(1, info.getId());
            statement.setString(2, info.getName());
            statement.setString(3, info.getPath());
            statement.setBoolean(4, info.isRunning());
            statement.executeUpdate();
            return info.getId();
        }
    }

    /**
     * Closes the connection pool
     * @throws SQLException
     */
    public void shutdown() throws SQLException
    {
        ds.close();
    }

    
}