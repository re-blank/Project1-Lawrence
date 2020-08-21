package com.project1.lawrencedang;

public class ProcessInfo {
    private int id;
    private String name;
    private boolean running;
    
    public ProcessInfo(int id, String name, boolean running) {
        this.id = id;
        this.name = name;
        this.running = running;
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

}