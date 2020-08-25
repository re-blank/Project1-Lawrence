package com.project1.lawrencedang.web;

import com.project1.lawrencedang.ProcessInfo;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInfoRepository {
    private List<ProcessInfo> mockDB;
    private int mock_id_count;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProcessInfoRepository()
    {
        mockDB = Collections.synchronizedList(new ArrayList<ProcessInfo>());
        mock_id_count = 0;
    }

    public ProcessInfo get(int id) throws APIException
    {
        try
        {
            return mockDB.get(id);
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new APIException("Resource with id does not exist.");
        }
        
    }

    public boolean put(ProcessInfo info) throws APIException
    {
        try
        {
            if(info.getId() >= 0 && info.getId() < mockDB.size())
            {
                mockDB.set(info.getId(), info);
                return true;
            }
            else 
            {
                throw new APIException("Resource with id does not exist");
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public int post(String name, String path, boolean running)
    {
        try
        {
            return add(name, path, running);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public synchronized int add(String name, String path, boolean running)
    {
        ProcessInfo p = new ProcessInfo(mock_id_count, name, path, running);
        mockDB.add(p);
        return mock_id_count++;
    }
    
}