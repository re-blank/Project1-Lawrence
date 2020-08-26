package com.project1.lawrencedang.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "request_logger", urlPatterns = {"/*"})
public class RequestLoggingFilter extends HttpFilter {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.debug("{} {} {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
        chain.doFilter(request, response);
        
    }
}