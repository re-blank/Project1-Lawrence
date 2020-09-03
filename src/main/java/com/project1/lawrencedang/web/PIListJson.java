package com.project1.lawrencedang.web;

import java.util.List;

import com.project1.lawrencedang.ProcessInfo;
/**
 * Wrapper class for a list of ProcessInfo. Used for serializing lists of ProcessInfo
 */
public class PIListJson {
    private List<ProcessInfo> processes;

    public PIListJson(List<ProcessInfo> list)
    {
        processes = list;
    }

    public List<ProcessInfo> getList()
    {
        return processes;
    }

    public void setList(List<ProcessInfo> lst)
    {
        processes = lst;
    }
}
