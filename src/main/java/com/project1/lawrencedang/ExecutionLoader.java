package com.project1.lawrencedang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ExecutionLoader {
    Map<Integer, ExecutionInfo> pMap;
    private String pathToDirectory;
    private String nameOfConfigFile;
    public ExecutionLoader(Map<Integer, ExecutionInfo> pMap)
    {
        this.pMap = pMap;
        pathToDirectory = System.getProperty("user.home")+File.separator+".hprocrunner";
        nameOfConfigFile = "executions.json";
    }

    public ExecutionLoader(Map<Integer, ExecutionInfo> pMap, String pathToDirectory, String executionFilename)
    {
        this.pMap = pMap;
        this.pathToDirectory = pathToDirectory;
        nameOfConfigFile = executionFilename;
    }

    public void loadProcessesIfExists() throws FileNotFoundException
    {
        File dir = new File(pathToDirectory);
        System.out.println(dir.getAbsolutePath());
        File executions = new File(pathToDirectory+File.separator+nameOfConfigFile);
        dir.mkdir();
        try
        {
            if(!executions.exists())
            {
                throw new FileNotFoundException("executions.json not found!");
            }
        }
        catch(IOException e)
        {
            System.err.println("Could not create");
        }
        Gson gson = new Gson();
        TypeToken mapType = new TypeToken<Map<String, List<ExecutionInfo>>>(){};
        Map<String, List<ExecutionInfo>> executionMap = gson.fromJson(new InputStreamReader(new FileInputStream(executions)), mapType.getType());
        int id = 0;
        for(ExecutionInfo info: executionMap.get("executions"))
        {
            pMap.put(id, info);
            id++;
        }
    }   
}