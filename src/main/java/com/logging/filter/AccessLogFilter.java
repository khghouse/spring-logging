package com.logging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * 모든 HTTP 요청의 메타 정보를 로깅하는 필터입니다.
 * HTTP 메서드, URI, 응답 코드, 처리 시간 등을 기록합니다.
 */
@Slf4j
@Order(2)
@Component
public class AccessLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        if ("GET".equalsIgnoreCase(method) && "/actuator/prometheus".equals(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String queryString = request.getQueryString();
            String fullUri = uri + (queryString != null ? "?" + queryString : "");
            int responseStatusCode = wrappedResponse.getStatus();

            log.info("[{}] {} [{}] ({}ms)", method, fullUri, HttpStatus.valueOf(responseStatusCode), duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

}
