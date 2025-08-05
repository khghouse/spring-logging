package com.logging.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Profile({"local", "test", "dev"})
public class StandardLoggingFilter extends RequestResponseLoggingFilter {

    @Override
    boolean shouldLogRequestResponse(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

}
