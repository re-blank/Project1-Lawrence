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
                logger.info("Started process {}", pi.getName());
                notifier.put(new ProcessNotification(new ProcessInfo(pi), false));
            }
            catch(IOException e)
            {
                logger.warn("Failed to start process {}", pi.getName());
                e.printStackTrace();
                return;
            }
            catch(InterruptedException e)
            {
                endProcess();
                Thread.currentThread().interrupt();
                return;
            }
        }
        
        try
        {
            process.waitFor();
            pi.setRunning(false);
            logger.debug("Process finished {}", pi.getName());
        }
        catch(InterruptedException e)
        {
            endProcess();
        }

        try
        {
            notifier.put(new ProcessNotification(new ProcessInfo(pi), false));
        }
        catch(InterruptedException e)
        {
            logger.debug("Interrupted while putting new state to listener");
            Thread.currentThread().interrupt();
        }
        finally
        {
            this.process = null;
        }
    }

    private void endProcess()
    {
        if(this.process.isAlive())
            this.process.destroy();

        pi.setRunning(false);
        logger.info("Stopped process {}", pi.getName());
        this.process = null;
    }
}