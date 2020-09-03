package com.project1.lawrencedang;

/**
 * ProcessInfo represents a single instance of a process created from an ExecutionInfo.
 */
public class ProcessInfo {
    private int id;
    private String name;
    private boolean running;
    private String path;
    
    /**
     * Creates a new ProcessInfo with the specified information
     * @param id
     * @param name
     * @param path
     * @param running
     */
    public ProcessInfo(int id, String name, String path, boolean running) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.running = running;
    }

    /**
     * Creates a copy of the supplied ProcessInfo
     * @param p
     */
    public ProcessInfo(ProcessInfo p)
    {
        this.id = p.id;
        this.name = p.name;
        this.running = p.running;
        this.path = p.path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(o instanceof ProcessInfo)
        {
            ProcessInfo pi = (ProcessInfo)o;
            return this.name.equals(pi.name) && this.id == pi.id && this.path.equals(pi.path) && this.running == pi.running;
        }

        return false;
    }

}