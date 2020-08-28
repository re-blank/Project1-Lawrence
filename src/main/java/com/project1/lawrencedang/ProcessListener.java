package com.project1.lawrencedang;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessListener implements Runnable {
    private Map<Integer, ExecutionInfo> processMap;

    private BlockingQueue<ProcessNotification> notificationQueue;
    private BlockingQueue<ProcessInfo> outputQueue;
    private Map<Integer, Future<?>> processRunnerThreads;
    private Map<Integer, ProcessRunner> processRunners;
    private ExecutorService threadPool;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProcessListener(BlockingQueue<ProcessNotification> processQueue, BlockingQueue<ProcessInfo> outputQueue, Map<Integer, ExecutionInfo> pMap)
    {
        processMap = pMap;
        notificationQueue = processQueue;
        this.outputQueue = outputQueue;
        processRunnerThreads = new HashMap<>();
        processRunners = new HashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
        initializeProcessRunners();
    }

    public ProcessListener(BlockingQueue<ProcessNotification> processQueue, BlockingQueue<ProcessInfo> outputQueue, 
                            Map<Integer, Future<?>> processThreads, Map<Integer, ProcessRunner> futures, ExecutorService threadPool, Map<Integer, ExecutionInfo> pMap)
    {
        processMap = pMap;
        notificationQueue = processQueue;
        this.outputQueue = outputQueue;
        processRunnerThreads = processThreads;
        processRunners = futures;
        this.threadPool = threadPool;
        initializeProcessRunners();
    }

    public void run()
    {
        while(true)
        {
            ProcessNotification notification; 
            try
            {
                notification = notificationQueue.take();
            }
            catch(InterruptedException e)
            {
                prematureShutdown();
                return;
            }
            
            ProcessInfo pi = notification.getProcessInfo();
            if(notification.isFromServlet()) // Run or terminate process
            {
                if(pi.isRunning())
                {
                    launch(pi);
                }
                else
                {
                    terminate(pi);
                }
            }
            else // Put info to output
            {
                try
                {
                    outputQueue.put(pi);
                }
                catch(InterruptedException e)
                {
                    prematureShutdown();
                    return;
                }
            }
            
        }
    }

    private void initializeProcessRunners()
    {
        for(Entry<Integer, ExecutionInfo> entry: processMap.entrySet())
        {
            ExecutionInfo exec = entry.getValue();
            String processPath = exec.getPathToFile();
            String interpreterString = exec.getInterpreter();
            ProcessBuilder builder;
            if(!interpreterString.equals(""))
            {
                builder = new ProcessBuilder(interpreterString, processPath);
            }
            else
            {
                builder = new ProcessBuilder(processPath);
            }
            
            File dir = new File(exec.getWorkingDir());
            builder.directory(dir);
            ProcessInfo pi = new ProcessInfo(entry.getKey(), exec.getName(), processPath, false);
            
            processRunners.put(entry.getKey(), new ProcessRunner(builder, pi, notificationQueue));
        }
    }

    private boolean launch(ProcessInfo pi)
    {
        if(!processRunnerThreads.containsKey(pi.getId()))
        {
            ProcessRunner runner = processRunners.get(pi.getId());
            Future<?> future = threadPool.submit(runner);
            processRunnerThreads.put(pi.getId(), future);
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean terminate(ProcessInfo pi)
    {
        if(processRunnerThreads.containsKey(pi.getId()))
        {
            processRunnerThreads.get(pi.getId()).cancel(true);
            processRunnerThreads.remove(pi.getId());
            return true;
        }
        else
        {
            return false;
        }
    }

    private void prematureShutdown()
    {
        logger.warn("Interrupted while updating process info.");
        threadPool.shutdownNow();
        return;
    }

    

}