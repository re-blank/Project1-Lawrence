package com.project1.lawrencedang;


import java.io.File;

import com.project1.lawrencedang.web.GetProcessInfoServlet;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class MainApp {
    public static void main(String[] args) throws LifecycleException {
        final String base = new File("./").getAbsolutePath();
        Tomcat server = new Tomcat();
        server.setPort(8081);
        server.getConnector();
        server.addWebapp("/project1", base);
        Wrapper wrapper = server.addServlet("/project1", "Test", new GetProcessInfoServlet());
        wrapper.addMapping("/api/process/*");
        wrapper.setLoadOnStartup(0);
        server.start();
        
    }
}