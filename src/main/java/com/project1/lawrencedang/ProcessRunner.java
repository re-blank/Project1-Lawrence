package com.project1.lawrencedang;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRunner implements Runnable {
    private ProcessInfo pi;
    private Process process;
    private ProcessBuilder builder;

    BlockingQueue<ProcessInfo> notifier;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProcessRunner(ProcessBuilder builder, ProcessInfo pi, BlockingQueue<ProcessInfo> sharedQueue)
    {
        this.builder = builder;
        this.pi = pi;
        this.notifier = sharedQueue;
    }

    public ProcessInfo getProcessInfo()
    {
        return this.pi;
    }

    public void run()
    {
        synchronized(this)
        {
            try
            {
                this.process = builder.start();
            }
            catch(IOException e)
            {
                logger.warn("Failed to start process {}", pi.getName());
                return;
            }
            pi.setRunning(true);
        }
        
        try
        {
            process.waitFor();
        }
        catch(InterruptedException e)
        {
            // What if it is stuck? Might have to forcibly destroy after x amt of time.
            process.destroy();
        }
        pi.setRunning(false);
        notifier.add(new ProcessInfo(pi));
    }

}