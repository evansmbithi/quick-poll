package com.apress.quickpoll.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Custom Exception
 * @ResponseStatus instructs Spring MVC that an HttpStatus NOT_FOUND (404 code) 
 * should be used in the response when a ResourceNotFoundException is thrown.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ResourceNotFoundException(){}

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
