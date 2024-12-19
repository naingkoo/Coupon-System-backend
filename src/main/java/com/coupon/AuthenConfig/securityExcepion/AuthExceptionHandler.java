package com.coupon.AuthenConfig.securityExcepion;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

public class AuthExceptionHandler implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Log the error with detailed information
        log.error("Unauthorized access attempt: {}", authException.getMessage());
        System.out.println("here is AuthExceptionHandler"+authException.getMessage());

        // Default response status and message
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        String errorMessage = "Unauthorized access - please provide valid credentials";

        // Check if the exception is of a specific type and modify response accordingly
        if (authException instanceof BadCredentialsException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            errorMessage = "Invalid credentials provided";
        } else if (authException instanceof InsufficientAuthenticationException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            errorMessage = "Insufficient authentication to access this resource";
        }

        // Set the response status
        response.setStatus(statusCode);

        // Set the content type to JSON
        response.setContentType("application/json");

        // Construct the error message
        String jsonResponse = String.format("{ \"error\": \"%s\", \"message\": \"%s\" }", errorMessage, authException.getMessage());

        // Write the error message to the response
        response.getWriter().write(jsonResponse);
    }
}
