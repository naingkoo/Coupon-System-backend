package com.coupon.controllerExecption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    // Custom error response class
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Exception handler method
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred.");

        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
