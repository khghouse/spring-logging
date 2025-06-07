package com.logging.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Profile({"local", "test"})
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method) && "/actuator/prometheus".equals(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logRequest(wrappedRequest);
        logResponse(wrappedResponse);

        wrappedResponse.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String prettyBody = toPrettyJson(body);

        if (prettyBody.isEmpty()) {
            log.info(">>> [HTTP Request] {} {}", request.getMethod(), request.getRequestURI());
        } else {
            log.info(">>> [HTTP Request] {} {}\n{}", request.getMethod(), request.getRequestURI(), prettyBody);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        String prettyBody = toPrettyJson(body);

        if (prettyBody.isEmpty()) {
            log.info("<<< [HTTP Response] {}", response.getStatus());
        } else {
            log.info("<<< [HTTP Response] {}\n{}", response.getStatus(), prettyBody);
        }
    }

    private String toPrettyJson(String json) {
        try {
            return objectMapper.readTree(json)
                    .toPrettyString();
        } catch (Exception e) {
            return json;
        }
    }

}
