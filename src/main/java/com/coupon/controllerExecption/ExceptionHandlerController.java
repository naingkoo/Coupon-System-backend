package com.coupon.controllerExecption;

import com.coupon.responObject.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
        System.out.println("handleUnknownException from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(BadCredentialsException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse("The Username or Password is Incorrect. Try again..");
        System.out.println("BadCredentialsException from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(DataIntegrityViolationException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse("duplicate email");
        System.out.println("BadCredentialsException from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(UsernameNotFoundException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        System.out.println("UsernameNotFoundException from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUNoResourceFoundException(HttpRequestMethodNotSupportedException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        System.out.println("HttpRequestMethodNotSupportedException from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExHandler(ResourceNotFoundException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        System.out.println("resourceNotFoundExHandler from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExHandler(IllegalArgumentException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        System.out.println("IllegalArgumentExHandler from metnhod :"+ex);
        // Return response with 500 Internal Server Error status and the error message
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
