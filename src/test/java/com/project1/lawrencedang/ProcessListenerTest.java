package com.project1.lawrencedang;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProcessListenerTest {
    ProcessListener listener;
    BlockingQueue<ProcessNotification> inQueue;
    BlockingQueue<ProcessInfo> outQueue;
    ExecutorService threadPool;

    public void setup()
    {
        inQueue = new LinkedBlockingQueue<>(5);
        outQueue = new LinkedBlockingQueue<>(5);
        threadPool = Executors.newCachedThreadPool();
        Map<Integer, Future<?>> futures = new HashMap<>();
        Map<Integer, ProcessRunner> runners = new HashMap<>();
        listener = new ProcessListener(inQueue, outQueue, futures, runners, threadPool);
    }

    public void outputsCorrectProcessInfo() throws InterruptedException
    {
        ProcessInfo testProcess = new ProcessInfo(1, "echo", "echo", true);
        ProcessNotification testNotif = new ProcessNotification(testProcess, true);
        listener.run();
        inQueue.put(testNotif);
        // Sent on process start
        ProcessInfo first = outQueue.poll(2, TimeUnit.SECONDS);
        assertEquals(testProcess, first);

        // Sent on process finish
        ProcessInfo second = outQueue.poll(2, TimeUnit.SECONDS);
        assertEquals(new ProcessInfo(1, "echo", "echo", false), second);
    }
}