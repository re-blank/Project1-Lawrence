package com.project1.lawrencedang;

public class ProcessUpdate {
    public enum UpdateType
    {
        REPLACE, NEW;
    }
    private ProcessInfo pi;
    private UpdateType updateType;

    public ProcessUpdate(ProcessInfo pi, UpdateType updateType)
    {
        this.pi = pi;
        this.updateType = updateType;
    }

    public ProcessInfo getPi() {
        return pi;
    }

    public void setPi(ProcessInfo pi) {
        this.pi = pi;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    
}