package com.project1.lawrencedang.web;

import com.project1.lawrencedang.ProcessInfo;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ProcessInfoRepository {
    private List<ProcessInfo> mockDB;
    private int mock_id_count;

    public ProcessInfoRepository()
    {
        mockDB = Collections.synchronizedList(new ArrayList<ProcessInfo>());
        mock_id_count = 0;
    }

    public ProcessInfo get(int id)
    {
        if(id >= mockDB.size())
        {
            return null;
        }
        else
        {
            return mockDB.get(id);
        }
    }

    public synchronized int add(String name, boolean running)
    {
        ProcessInfo p = new ProcessInfo(mock_id_count, name, running);
        mockDB.add(p);
        return mock_id_count++;
    }
    
}