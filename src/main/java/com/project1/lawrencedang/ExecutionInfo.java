package com.project1.lawrencedang;

import java.util.Arrays;

public class ExecutionInfo {
    private String interpreter;
    private String filename;
    private String pathToDir;
    private String workingDir;
    private String name;
    private String[] interpreterArgs;
    private String[] args;

    public ExecutionInfo(String name, String pathToDir, String filename, String workingDir) {
        this.interpreter = "";
        this.pathToDir = pathToDir;
        this.name = name;
        this.filename = filename;
        this.workingDir = workingDir;
        this.interpreterArgs = null;
        this.args = null;
    }

    public ExecutionInfo(String name, String pathToDir, String filename, String workingDir, String[] args) {
        this.interpreter = "";
        this.pathToDir = pathToDir;
        this.name = name;
        this.filename = filename;
        this.workingDir = workingDir;
        this.interpreterArgs = null;
        this.args = args;
    }

    public ExecutionInfo(String name, String pathToDir, String interpreter, String filename, String workingDir, String[] interpreterArgs, String[] args) {
        this.interpreter = interpreter;
        this.pathToDir = pathToDir;
        this.name = name;
        this.filename = filename;
        this.workingDir = workingDir;
        this.interpreterArgs = interpreterArgs;
        this.args = args;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public String getPathToDir() {
        return pathToDir;
    }

    public void setPathToDir(String pathToDir) {
        this.pathToDir = pathToDir;
    }

    public String[] getInterpreterArgs() {
        return interpreterArgs;
    }

    public void setInterpreterArgs(String[] interpreterArgs) {
        this.interpreterArgs = interpreterArgs;
    }
    

    @Override
    public boolean equals(Object obj) {
        //auto-generated
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExecutionInfo other = (ExecutionInfo) obj;
        if (!Arrays.equals(args, other.args))
            return false;
        if (filename == null) {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        if (interpreter == null) {
            if (other.interpreter != null)
                return false;
        } else if (!interpreter.equals(other.interpreter))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (workingDir == null) {
            if (other.workingDir != null)
                return false;
        } else if (!workingDir.equals(other.workingDir))
            return false;
        return true;
    }

    
}