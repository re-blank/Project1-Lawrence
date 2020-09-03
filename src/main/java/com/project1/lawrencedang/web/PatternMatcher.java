package com.project1.lawrencedang.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for pattern regex.
 */
public class PatternMatcher {
    private static final Pattern getIdPattern = Pattern.compile("/([0-9]+)");

    /**
     * Returns the request id, which is an integer at the end of the path info.
     * @param pathInfo
     * @return Request id, or -1 if there is no match.
     */
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