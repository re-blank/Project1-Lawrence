package com.project1.lawrencedang.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project1.lawrencedang.ProcessInfo;

@WebServlet(name = "Test", value="/api/process/*")
public class TestServlet extends HttpServlet
{
    Pattern getIdPattern = Pattern.compile("/([0-9]+)");
    ProcessInfoRepository repo;
    @Override
    public void init() throws ServletException {
        repo = new ProcessInfoRepository();
        repo.add("test1", false);
        repo.add("test2", false);
        repo.add("test3", false);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getPathInfo());
        int id = getRequestId(req.getPathInfo());
        if(id == -1)
        {
            resp.sendError(400);
        }
        else
        {
            ProcessInfo process = repo.get(id);
            if(process == null)
            {
                resp.sendError(404);
            }
            Gson gson = new Gson();
            resp.getWriter().println(gson.toJson(process));
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
