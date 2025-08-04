package com.logging.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public abstract class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Set<String> MASKING_FIELDS = Set.of("description");

    private final ObjectMapper objectMapper = new ObjectMapper();

    abstract boolean shouldLogRequestResponse(HttpServletRequest request, HttpServletResponse response);

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

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            if (shouldLogRequestResponse(request, response)) {
                logRequest(wrappedRequest);
                logResponse(wrappedResponse);
            }
            wrappedResponse.copyBodyToResponse();
        }
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
            JsonNode jsonNode = objectMapper.readTree(json);
            maskSensitiveFields(jsonNode);
            return jsonNode.toPrettyString();
        } catch (Exception e) {
            return json;
        }
    }

    private void maskSensitiveFields(JsonNode node) {
        if (node.isObject()) { // { ... } 형태일 경우
            ObjectNode objectNode = (ObjectNode) node; // JsonNode (Immutable) -> ObjectNode (Mutable)
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if (value.isValueNode()) { // 단순 값 형태
                    if (MASKING_FIELDS.contains(key.toLowerCase())) {
                        objectNode.put(key, "[MASKED]");
                    }
                } else { // { ... }, [ ... ]
                    if (value.isObject()) {
                        maskSensitiveFields(value);
                    } else if (value.isArray()) {
                        for (JsonNode item : value) {
                            maskSensitiveFields(item);
                        }
                    }
                }
            }
        } else if (node.isArray()) { // [ ... ] 형태일 경우
            for (JsonNode item : node) {
                maskSensitiveFields(item);
            }
        }
    }

}
