package com.project1.lawrencedang.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.project1.lawrencedang.ProcessInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class ProcessInfoRepositoryTest
{
    ProcessInfoRepository repo;
    @BeforeEach
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

    @Test
    public void getOutOfBoundsThrowsAPIException() throws APIException
    {
        assertThrows(APIException.class, () -> repo.get(-1));
    }

    @Test
    public void putCannotAddNewProcess() throws APIException
    {
        ProcessInfo pi = new ProcessInfo(100, "put", "/", true);
        assertThrows(APIException.class, () -> repo.put(pi));
    }
}
