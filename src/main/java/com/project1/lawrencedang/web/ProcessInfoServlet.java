package com.project1.lawrencedang.web;

import static com.project1.lawrencedang.web.PatternMatcher.getRequestId;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.project1.lawrencedang.DBUpdater;
import com.project1.lawrencedang.ExecutionInfo;
import com.project1.lawrencedang.ExecutionLoader;
import com.project1.lawrencedang.ProcessInfo;
import com.project1.lawrencedang.ProcessListener;
import com.project1.lawrencedang.ProcessNotification;
import com.project1.lawrencedang.ProcessUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main public-facing interface to the application. Handles incoming requests and responds.
 * Initializes necessary background threads to process requests during servlet initialization.
 */
@WebServlet(name = "Test", value="/api/process/*", loadOnStartup = 0)
public class ProcessInfoServlet extends HttpServlet
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    Gson gson;
    
    private ExecutorService threadPool; 
    BlockingQueue<ProcessNotification> sharedQueue;
    ProcessInfoRepository repo;

    /**
     * Initializes the resources necessary for this application to function.
     * Sets up threads for Process launching, database updating, and sets up communication channels between them.
     */
    @Override
    public void init() throws ServletException {
        System.out.println("Initializing servlet");
        threadPool = Executors.newFixedThreadPool(2);
        gson = new Gson();
        BlockingQueue<ProcessUpdate> outputQueue = new LinkedBlockingQueue<>();
        sharedQueue = new LinkedBlockingQueue<>();
        HashMap<Integer, ExecutionInfo> pMap = new HashMap<>();
        ExecutionLoader loader = new ExecutionLoader(pMap);
        try
        {
            loader.loadProcessesIfExists();
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Could not find execution json!");
            logger.error("Could not load executions");
            throw new ServletException("Failed to load executions");
        }
        ProcessListener listener = new ProcessListener(sharedQueue, outputQueue, pMap);

        try
        {
            repo = new ProcessInfoRepository();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            logger.error("Could not connect to database.");
            throw new ServletException("Failed to initialize Servlet.");
        }
        DBUpdater updater = new DBUpdater(outputQueue, repo);

        threadPool.execute(updater);
        threadPool.execute(listener);
        System.out.println("Finished initializing servlet");
    }

    /**
     * Shuts down this servlet, trying to close database connections and clean up threads.
     */
    @Override
    public void destroy() {
        
        try
        {
            repo.shutdown();
        }
        catch(SQLException e)
        {
            logger.warn("Database did not shut down properly.");
        }
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getPathInfo());
        if(req.getPathInfo() == null || req.getPathInfo().equals("/"))
        {
            PIListJson lst;
            try
            {
                lst = new PIListJson(repo.get());
            }
            catch(SQLException e)
            {
                System.err.println("Failed to access database");
                resp.sendError(404);
                return;
            }
            resp.getWriter().println(gson.toJson(lst));
        }
        else
        {
            int id = getRequestId(req.getPathInfo());
            if(id <= -1)
            {
                resp.sendError(404);
            }
            else
            {
                ProcessInfo process;
                try
                {  
                    process = repo.get(id);
                }
                catch(SQLException e)
                {
                    logger.warn("Failed to access to database");
                    resp.sendError(404);
                    return;
                }
                if(process == null)
                {
                    resp.sendError(404);
                    return;
                }
                resp.setContentType("application/json");
                resp.getWriter().println(gson.toJson(process));
            }            
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int id = getRequestId(req.getPathInfo());
        ProcessInfo pi;
        try
        {
            Reader reader = req.getReader();
            pi = gson.fromJson(reader, ProcessInfo.class);
        }
        catch(JsonSyntaxException e)
        {
            resp.sendError(400);
            return;
        }
        if(id <= -1)
        {
            resp.sendError(404);
            return;
        }
        if(id != pi.getId())
        {
            resp.sendError(400);
            return;
        }
        // Check if this process matches one in db
        try
        {
            ProcessInfo compare = repo.get(pi.getId());
            if(!(compare != null && compare.getName().equals(pi.getName()) && compare.getPath().equals(pi.getPath())))
            {  
                resp.sendError(400);
                return;
            }
        }
        catch(SQLException e)
        {
            logger.warn("Failed to access database.");
            return;
        }

        

        boolean success = false;
        try
        {
            success = sharedQueue.offer(new ProcessNotification(pi, true), 5, TimeUnit.SECONDS);
        }
        catch(InterruptedException e)
        {
            logger.info("Tried to interrupt Servlet while offering update");
        }
        if(success)
        {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else
        {
            resp.sendError(500);
        }
        return;
    }
}
