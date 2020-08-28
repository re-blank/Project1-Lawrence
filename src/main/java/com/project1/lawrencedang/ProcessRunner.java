package com.project1.lawrencedang;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRunner implements Runnable {
    private ProcessInfo pi;
    private Process process;
    private ProcessBuilder builder;

    BlockingQueue<ProcessNotification> notifier;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProcessRunner(ProcessBuilder builder, ProcessInfo pi, BlockingQueue<ProcessNotification> sharedQueue)
    {
        this.builder = builder;
        this.pi = pi;
        this.notifier = sharedQueue;
    }

    public ProcessInfo getProcessInfo()
    {
        return this.pi;
    }

    public boolean isRunning()
    {
        return this.pi.isRunning();
    }

    public void run()
    {
        synchronized(this)
        {
            try
            {
                this.process = builder.start();
                pi.setRunning(true);
                notifier.put(new ProcessNotification(new ProcessInfo(pi), false));
                logger.info("Started process {}", pi.getName());
            }
            catch(IOException e)
            {
                logger.warn("Failed to start process {}", pi.getName());
                return;
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        
        try
        {
            process.waitFor();
            logger.debug("Process finished {}", pi.getName());
        }
        catch(InterruptedException e)
        {
            // What if it is stuck? Might have to forcibly destroy after x amt of time.
            process.destroy();
            logger.info("Stopped process {}", pi.getName());
        }
        pi.setRunning(false);
        try
        {
            notifier.put(new ProcessNotification(new ProcessInfo(pi), false));
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        this.process = null;
    }

}