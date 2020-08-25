package com.project1.lawrencedang.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.project1.lawrencedang.ProcessInfo;

import org.junit.Before;
import org.junit.Test;

public class PutRequestHandlerTest {
    ProcessInfoRepository repo;
    PutRequestHandler handler;
    @Before
    public void setup()
    {
        repo = new ProcessInfoRepository();
        handler = new PutRequestHandler(null, repo);
    }
    @Test
    public void bodyMatchReturnsCorrectResult() throws Exception
    {
        
        int id= repo.post("test", "/", false);
        ProcessInfo pi = new ProcessInfo(id, "test", "/", true);
        handler.setProcessInfo(pi);
        assertTrue(handler.bodyMatchesExistingResource());

        ProcessInfo bad = new ProcessInfo(id, "bad", "/", true);
        handler.setProcessInfo(bad);
        assertFalse(handler.bodyMatchesExistingResource());
        
    }

    @Test
    public void tryPutSucceeds() throws APIException
    {
        int id = repo.post("test", "/", false);
        ProcessInfo pi = new ProcessInfo(id, "test", "/", true);
        handler.setProcessInfo(pi);
        handler.tryPut();
        assertEquals(pi, repo.get(id));
    }

    @Test(expected = PutModificationException.class)
    public void tryPutThrows() throws PutCreationException, PutModificationException
    {
        int id = repo.post("test", "/", false);
        ProcessInfo pi = new ProcessInfo(id, "bad", "/", true);
        handler.setProcessInfo(pi);
        handler.tryPut();
    }

    @Test(expected = PutCreationException.class)
    public void bodyMatchThrows() throws PutCreationException
    {
        ProcessInfo pi = new ProcessInfo(100, "test", "/", false);
        handler.setProcessInfo(pi);
        handler.bodyMatchesExistingResource();
    }

    
}