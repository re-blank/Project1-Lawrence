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

/**
 * ExecutionLoader converts the "executions.json" file into a mapping containing ExecutionInfo objects.
 */
public class ExecutionLoader {
    Map<Integer, ExecutionInfo> pMap;
    private String pathToDirectory;
    private String nameOfConfigFile;
    /**
     * Create a new ExecutionLoader that will attempt to read the "executions.json" file in the default "${user.home}/.hprocrunner/" directory.
     * @param pMap the processMap to fill.
     */
    public ExecutionLoader(Map<Integer, ExecutionInfo> pMap)
    {
        this.pMap = pMap;
        pathToDirectory = System.getProperty("user.home")+File.separator+".hprocrunner";
        nameOfConfigFile = "executions.json";
    }

    /**
     * Create a new ExecutionLoader that will attempt to read the specified file in the specified directory.
     * @param pMap
     * @param pathToDirectory
     * @param executionFilename
     */
    public ExecutionLoader(Map<Integer, ExecutionInfo> pMap, String pathToDirectory, String executionFilename)
    {
        this.pMap = pMap;
        this.pathToDirectory = pathToDirectory;
        nameOfConfigFile = executionFilename;
    }

    /**
     * Converts the JSON in the file to a Map, and store it in the supplied map. 
     * @throws FileNotFoundException
     */
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