package com.project1.lawrencedang;


import java.io.File;
import java.util.Optional;

import com.project1.lawrencedang.web.ProcessInfoServlet;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class MainApp {
    public static void main(String[] args) throws LifecycleException {
        int port = 8081;
        if(args.length > 0)
        {
            port = Integer.valueOf(args[0]);
        }
        final String base = new File("./").getAbsolutePath();
        Tomcat server = new Tomcat();
        server.setPort(Integer.valueOf(port));
        server.getConnector();
        server.addWebapp("/project1", base);
        Wrapper wrapper = server.addServlet("/project1", "Test", new ProcessInfoServlet());
        wrapper.addMapping("/api/process/*");
        wrapper.setLoadOnStartup(0);
        server.start();
        
    }
}