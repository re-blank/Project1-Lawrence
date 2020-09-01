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
        loader.loadProcessesIfExists();
        ExecutionInfo info = new ExecutionInfo("test.exe","testInterpreter", "test", "test1/test2", args);
        assertEquals(info, pMap.get(0));
    }
}