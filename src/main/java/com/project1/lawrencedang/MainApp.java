package com.project1.lawrencedang;


import java.io.File;

import com.project1.lawrencedang.web.GetProcessInfoServlet;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class MainApp {
    public static void main(String[] args) throws LifecycleException {
        Tomcat server = new Tomcat();
        server.setBaseDir(new File("target/tomcat/").getAbsolutePath());
        server.setPort(8081);
        server.getConnector();
        server.addWebapp("/project1", new File("src/main/resources/").getAbsolutePath());
        Wrapper wrapper = server.addServlet("/project1", "Test", new GetProcessInfoServlet());
        wrapper.addMapping("/api/process/*");
        wrapper.setLoadOnStartup(0);
        server.start();
        
    }
}