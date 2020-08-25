package com.project1.lawrencedang.web;

import com.project1.lawrencedang.ProcessInfo;

public class PutRequestHandler
{
    ProcessInfo pi;
    ProcessInfoRepository repo;
    public PutRequestHandler(ProcessInfo pi, ProcessInfoRepository repo)
    {
        this.pi = pi;
        this.repo = repo;
    }

    public void setProcessInfo(ProcessInfo pi)
    {
        this.pi = pi;
    }

    public boolean bodyMatchesExistingResource() throws PutCreationException
    {
        try
        {
            ProcessInfo inDB = repo.get(pi.getId());
            return pi.getId() == inDB.getId() && pi.getName().equals(inDB.getName()) && pi.getPath().equals(inDB.getPath());
        }
        catch(APIException e)
        {
            throw new PutCreationException("Put request tried to create new resource");
        }
        
    }

    public void tryPut() throws PutCreationException, PutModificationException
    {
        if(bodyMatchesExistingResource())
        {
            try
            {
                repo.put(this.pi);
            }
            catch(APIException e)
            {
                System.err.println("Should never get here");
                return;
            }
        }
        else
        {
            throw new PutModificationException("Request tries to modify unmodifiable fields");
        }
    }
}