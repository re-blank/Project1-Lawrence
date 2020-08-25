package com.project1.lawrencedang.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.project1.lawrencedang.ProcessInfo;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ProcessInfoRepositoryTest
{
    ProcessInfoRepository repo;
    @Before
    public void setup()
    {
        repo = new ProcessInfoRepository();
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void postsAndGetsSameProcess() throws APIException
    {
        int id = repo.post("test", "/", true);
        ProcessInfo pi = new ProcessInfo(id, "test", "/", true);
        ProcessInfo gotObj = repo.get(id);
        assertEquals(pi, gotObj);
    }

    @Test
    public void putReplacesProcess() throws APIException
    {
        int id = repo.post("test", "/", true);
        ProcessInfo pi = new ProcessInfo(id, "put", "/", true);
        repo.put(pi);
        ProcessInfo gotObj = repo.get(id);
        assertEquals(pi, gotObj);
    }

    @Test(expected = APIException.class)
    public void getOutOfBoundsThrowsAPIException() throws APIException
    {
        repo.get(-1);
    }

    @Test(expected = APIException.class)
    public void putCannotAddNewProcess() throws APIException
    {
        ProcessInfo pi = new ProcessInfo(100, "put", "/", true);
        repo.put(pi);
    }
}
