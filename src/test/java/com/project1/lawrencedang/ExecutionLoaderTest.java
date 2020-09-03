package com.project1.lawrencedang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class ExecutionLoaderTest {
    
    @Test
    public void testLoading() throws FileNotFoundException
    {
        HashMap<Integer, ExecutionInfo> pMap = new HashMap<>();
        ExecutionLoader loader = new ExecutionLoader(pMap, "src/test/java/com/project1/lawrencedang/testFiles", "executions.json");
        String[] args = {"test1", "test2"};
        String[] iargs = {"i1","i2"};
        loader.loadProcessesIfExists();
        ExecutionInfo info = new ExecutionInfo("test.exe","test1/test2", "testInterpreter", "test", "test1/test2", iargs,args);
        assertEquals(info, pMap.get(0));
    }

    @Test
    public void testEmptyExecution() throws FileNotFoundException
    {
        HashMap<Integer, ExecutionInfo> pMap = new HashMap<>();
        ExecutionLoader loader = new ExecutionLoader(pMap, "src/test/java/com/project1/lawrencedang/testFiles", "executions2.json");
        String[] args = new String[0];
        String[] iargs = new String[0];
        loader.loadProcessesIfExists();
        ExecutionInfo info = new ExecutionInfo("","", "", "", "", iargs,args);
        assertEquals(info, pMap.get(0));
    }
}