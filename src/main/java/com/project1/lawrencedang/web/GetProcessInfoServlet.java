package com.project1.lawrencedang.web;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.project1.lawrencedang.ProcessInfo;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;

@WebServlet(name = "Test", value="/api/process/*")
public class GetProcessInfoServlet extends HttpServlet
{
    Pattern getIdPattern = Pattern.compile("/([0-9]+)");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    ProcessInfoRepository repo;
    Gson gson;
    @Override
    public void init() throws ServletException {
        gson = new Gson();
        repo = new ProcessInfoRepository();
        repo.add("test1", "/", false);
        repo.add("test2", "/test", false);
        repo.add("test3", "/",  false);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getPathInfo());
        int id = getRequestId(req.getPathInfo());
        if(id <= -1)
        {
            if(req.getPathInfo() == null)
            {
                resp.sendError(405);
            }
            else
            {
                resp.sendError(404);
            }
        }
        else
        {
            ProcessInfo process;
            try
            {  
                process = repo.get(id);
            }
            catch(APIException e)
            {
                resp.sendError(404);
                return;
            }

            resp.getWriter().println(gson.toJson(process));
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
        try
        {
            new PutRequestHandler(pi, repo).tryPut();
        }
        catch(PutCreationException e)
        {
            resp.sendError(400);
        }
        catch(PutModificationException e)
        {
            logger.info("Illegal put modification: {}", gson.toJson(pi));
            resp.sendError(400);
        }
    }

    public int getRequestId(String path)
    {
        Matcher matcher = getIdPattern.matcher(path);
        if(matcher.matches())
        {
            return Integer.parseInt(matcher.group(1));
        }

        return -1;
    }
}
