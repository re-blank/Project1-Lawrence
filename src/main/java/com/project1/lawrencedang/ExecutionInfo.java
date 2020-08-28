package com.project1.lawrencedang;

public class ExecutionInfo {
    private String interpreter;
    private String pathToFile;
    private String workingDir;
    private String name;
    private String[] args;
    
    public ExecutionInfo(String name, String pathToFile, String workingDir) {
        this.interpreter = "";
        this.name = name;
        this.pathToFile = pathToFile;
        this.workingDir = workingDir;
        this.args = null;
    }

    public ExecutionInfo(String name, String pathToFile, String workingDir, String[] args) {
        this.interpreter = "";
        this.name = name;
        this.pathToFile = pathToFile;
        this.workingDir = workingDir;
        this.args = args;
    }
    public ExecutionInfo(String name, String interpreter, String pathToFile, String workingDir, String[] args) {
        this.interpreter = interpreter;
        this.name = name;
        this.pathToFile = pathToFile;
        this.workingDir = workingDir;
        this.args = args;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}