package com.project1.lawrencedang;

/**
 * Wrapper class for ProcessInfo, adding information about the source of the ProcessInfo
 */
public class ProcessNotification {
    ProcessInfo pi;
    boolean fromServlet;

    public ProcessNotification(ProcessInfo pi, boolean fromServlet) {
        this.pi = pi;
        this.fromServlet = fromServlet;
    }

    public ProcessInfo getProcessInfo() {
        return pi;
    }

    public boolean isFromServlet() {
        return fromServlet;
    }

    
}