package com.project1.lawrencedang;

import com.project1.lawrencedang.ProcessUpdate.UpdateType;
import com.project1.lawrencedang.web.ProcessInfoRepository;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Takes updates from ProcessListener and pushes them to the database.
 */
public class DBUpdater implements Runnable {
    BlockingQueue<ProcessUpdate> updateQueue;
    ProcessInfoRepository repo;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public DBUpdater(BlockingQueue<ProcessUpdate> updateQueue, ProcessInfoRepository repo)
    {
        this.updateQueue = updateQueue;
        this.repo = repo;
    }

    public void run()
    {
        while(true)
        {
            ProcessUpdate update;
            try
            {
                update = updateQueue.take();
                logger.debug("Got update info for process {} (name: {})", update.getPi().getId(), update.getPi().getName());
            }
            catch(InterruptedException e)
            {
                return;
            }
            try
            {
                if(update.getUpdateType() ==UpdateType.NEW)
                {
                    repo.post(update.getPi());
                }
                else if(update.getUpdateType() == UpdateType.REPLACE)
                {
                    repo.put(update.getPi());
                }
                logger.debug("Successfully inserted update");
            }
            catch(SQLException e)
            {
                logger.warn("Failed to access database");
            }
        }

    }
}