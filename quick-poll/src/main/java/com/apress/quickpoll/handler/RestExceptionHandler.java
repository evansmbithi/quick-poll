package com.apress.quickpoll.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apress.quickpoll.customException.ResourceNotFoundException;
import com.apress.quickpoll.dto.error.ErrorDetail;

import jakarta.servlet.http.HttpServletRequest;

/*
 * We need an application-wide strategy that handles all of the errors 
 * in the same way and writes the associated details to the response body. 
 * Classes annotated with @ControllerAdvice can be used to implement such 
 * crosscutting concerns
 */
@ControllerAdvice
public class RestExceptionHandler {
    /*
     * Thanks to the @ExceptionHandler annotation, any time a 
     * ResourceNotFoundException is thrown by a controller, 
     * Spring MVC would invoke the RestExceptionHandler’s 
     * handleResourceNotFoundException method. Inside this 
     * method, we create an instance of ErrorDetail and populate 
     * it with error information.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime()); // set time in milliseconds
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("Resource Not Found");
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setDeveloperMessage(rnfe.getClass().getName());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }    
}
