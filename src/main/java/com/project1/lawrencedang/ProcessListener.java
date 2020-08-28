package com.project1.lawrencedang;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessListener implements Runnable {
    private Map<Integer, ProcessInfo> processMap;

    private BlockingQueue<ProcessNotification> notificationQueue;
    private BlockingQueue<ProcessInfo> outputQueue;
    private Map<Integer, Future<?>> processRunnerThreads;
    private Map<Integer, ProcessRunner> processRunners;
    private ExecutorService threadPool;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProcessListener(BlockingQueue<ProcessNotification> processQueue, BlockingQueue<ProcessInfo> outputQueue, Map<Integer, ProcessInfo> pMap)
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
                            Map<Integer, Future<?>> processThreads, Map<Integer, ProcessRunner> futures, ExecutorService threadPool, Map<Integer, ProcessInfo> pMap)
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
                    boolean launched = launch(pi);
                }
                else
                {
                    boolean terminated = terminate(pi);
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
                }
                return;
            }
            
        }
    }

    private void initializeProcessRunners()
    {
        for(Entry<Integer, ProcessInfo> entry: processMap.entrySet())
        {
            String processPath = entry.getValue().getPath();
            ProcessBuilder builder = new ProcessBuilder(processPath);
            processRunners.put(entry.getKey(), new ProcessRunner(builder, entry.getValue(), notificationQueue));
        }
    }

    private boolean launch(ProcessInfo pi)
    {
        if(!processRunnerThreads.containsKey(pi.getId()))
        {
            ProcessRunner runner = processRunners.get(pi.getId());
            Future<?> future = threadPool.submit(runner);
            processRunners.put(pi.getId(), runner);
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
            processRunners.remove(pi.getId());
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