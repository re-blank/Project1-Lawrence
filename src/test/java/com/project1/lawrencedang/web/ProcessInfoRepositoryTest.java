package com.project1.lawrencedang.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import com.project1.lawrencedang.ProcessInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class ProcessInfoRepositoryTest
{
    ProcessInfoRepository repo;
    @BeforeEach
    public void setup() throws SQLException
    {
        try
        {
            repo = new ProcessInfoRepository();
        }
        catch(SQLException e)
        {
            System.err.println("Could not open database connection");
            e.printStackTrace();
            throw e;
        }
    }

    @AfterEach
    public void teardown() throws SQLException
    {
        repo.shutdown();
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void postsAndGetsSameProcess() throws SQLException
    {
        ProcessInfo pi = new ProcessInfo(0, "test", "/", true);
        ProcessInfo gotObj;
        try
        {
            int id = repo.post(pi);
            gotObj = repo.get(id);
        }
        catch(SQLException e)
        {
            System.err.println("Failed to access db.");
            throw e;
        }
        assertEquals(pi, gotObj);
    }

    @Test
    public void putUpdatesProcess() throws SQLException
    {
        ProcessInfo pi = new ProcessInfo(0, "put", "/", true);
        int id = repo.post(pi);
        ProcessInfo pi2 = new ProcessInfo(pi);
        pi2.setRunning(false);
        repo.put(pi2);
        pi.setRunning(false);
        assertEquals(pi, repo.get(0));
    }

    /*
    @Test
    public void putReplacesProcess() throws APIException
    {
        int id = repo.post("test", "/", true);
        ProcessInfo pi = new ProcessInfo(id, "put", "/", true);
        repo.put(pi);
        ProcessInfo gotObj = repo.get(id);
        assertEquals(pi, gotObj);
    }
    */

    @Test
    public void getNonexistentProcessReturnsNull() throws SQLException
    {
        assertNull(repo.get(-1));
    }

    @Test
    public void putCannotAddNewProcess() throws SQLException
    {
        ProcessInfo pi = new ProcessInfo(100, "put", "/", true);
        assertFalse(repo.put(pi));
    }
}
