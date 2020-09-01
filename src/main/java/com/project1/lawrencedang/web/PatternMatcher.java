package com.project1.lawrencedang.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    private static final Pattern getIdPattern = Pattern.compile("/([0-9]+)");

    public static int getRequestId(String pathInfo)
    {
        Matcher matcher = getIdPattern.matcher(pathInfo);
        if(matcher.matches())
        {
            return Integer.parseInt(matcher.group(1));
        }

        return -1;
    }
    
}