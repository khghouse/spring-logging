package com.logging.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "test", "dev"})
public class StandardLoggingFilter extends AbstractRequestResponseLoggingFilter {

    @Override
    boolean isLogRequestResponse(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

}
