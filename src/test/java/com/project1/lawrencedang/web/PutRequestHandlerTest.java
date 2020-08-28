package com.project1.lawrencedang.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project1.lawrencedang.ProcessInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PutRequestHandlerTest {
    ProcessInfoRepository repo;
    PutRequestHandler handler;
    @BeforeEach
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

    @Test
    public void tryPutThrows() throws PutCreationException, PutModificationException
    {
        int id = repo.post("test", "/", false);
        ProcessInfo pi = new ProcessInfo(id, "bad", "/", true);
        handler.setProcessInfo(pi);
        assertThrows(PutModificationException.class, () -> handler.tryPut()); 
    }

    @Test
    public void bodyMatchThrows() throws PutCreationException
    {
        ProcessInfo pi = new ProcessInfo(100, "test", "/", false);
        handler.setProcessInfo(pi);
        assertThrows(PutCreationException.class, () -> handler.bodyMatchesExistingResource());
    }

    
}