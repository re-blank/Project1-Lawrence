package com.project1.lawrencedang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProcessListenerTest {
    ProcessListener listener;
    BlockingQueue<ProcessNotification> inQueue;
    BlockingQueue<ProcessInfo> outQueue;
    ExecutorService threadPool;

    @BeforeEach
    public void setup()
    {
        inQueue = new LinkedBlockingQueue<>(5);
        outQueue = new LinkedBlockingQueue<>(5);
        threadPool = Executors.newCachedThreadPool();
        Map<Integer, Future<?>> futures = new HashMap<>();
        Map<Integer, ProcessRunner> runners = new HashMap<>();
        Map<Integer, ExecutionInfo> pMap = new HashMap<>();
        Path path = Paths.get(System.getProperty("java.home"), "bin");
        pMap.put(0, new ExecutionInfo("test", "java.exe", path.toString()));
        listener = new ProcessListener(inQueue, outQueue, futures, runners, threadPool, pMap);
    }

    @Test
    //@Timeout(value = 3, unit = TimeUnit.SECONDS)
    public void outputsCorrectProcessInfo() throws InterruptedException
    {
        ProcessInfo testProcess = new ProcessInfo(0, "test", "java.exe", true);
        ProcessNotification testNotif = new ProcessNotification(testProcess, true);
        Thread thread = new Thread(listener);
        thread.start();
        inQueue.put(testNotif);
        // Sent on process start
        ProcessInfo first = outQueue.poll(2, TimeUnit.SECONDS);
        assertEquals(testProcess, first);

        // Sent on process finish
        ProcessInfo second = outQueue.poll(2, TimeUnit.SECONDS);
        assertEquals(new ProcessInfo(0, "test", "java.exe", false), second);
    }
}