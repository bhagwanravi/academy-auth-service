package com.example.academy.auth.shared.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class TraceIdResponseFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-ID";
    private static final String TRACE_ID_ATTRIBUTE = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate or retrieve trace ID
        String traceId = getOrCreateTraceId(request);
        
        // Add trace ID to response header
        response.setHeader(TRACE_ID_HEADER, traceId);
        
        // Add trace ID to request attributes for downstream use
        request.setAttribute(TRACE_ID_ATTRIBUTE, traceId);
        
        // Log the request with trace ID
        log.info("Processing request: {} {} - Trace ID: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                traceId);
        
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error processing request with Trace ID: {}", traceId, e);
            throw e;
        }
        
        // Log the response completion
        log.info("Completed request: {} {} - Trace ID: {} - Status: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                traceId, 
                response.getStatus());
    }

    private String getOrCreateTraceId(HttpServletRequest request) {
        // Check if trace ID already exists in request headers
        String traceId = request.getHeader(TRACE_ID_HEADER);
        
        if (traceId == null || traceId.trim().isEmpty()) {
            // Generate new trace ID
            traceId = UUID.randomUUID().toString().replace("-", "");
            log.debug("Generated new Trace ID: {}", traceId);
        } else {
            log.debug("Using existing Trace ID: {}", traceId);
        }
        
        return traceId;
    }
}
