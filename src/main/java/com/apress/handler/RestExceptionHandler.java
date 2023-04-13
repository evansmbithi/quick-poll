package com.apress.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
// import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apress.dto.error.ErrorDetail;
import com.apress.dto.error.ValidationError;

/*
 * We need an application-wide strategy that handles all of the errors 
 * in the same way and writes the associated details to the response body. 
 * Classes annotated with @ControllerAdvice can be used to implement such 
 * crosscutting concerns
 * 
 * Thanks to the @ExceptionHandler annotation, any time a 
 * ResourceNotFoundException is thrown by a controller, 
 * Spring MVC would invoke the RestExceptionHandler’s 
 * handleResourceNotFoundException method. Inside this 
 * method, we create an instance of ErrorDetail and populate 
 * it with error information.
 * 
 * read the properties from quick-poll/src/main/resources/messages.properties 
 * and use them during the ValidationError instance creation
 * 
 * The property file approach not only simplifies Java code but also makes it 
 * easy to swap the messages without making code changes. 
 * 
 * Spring’s MessageSource provides an abstraction to easily resolve messages
 * We are using MessageResource's getMessage method to retrieve messages
 */

 /*
@ControllerAdvice
public class RestExceptionHandler {

    @Inject
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDetail handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri");

        if(requestPath == null) {
            requestPath = request.getRequestURI();
        }
        errorDetail.setTitle("Validation Failed");
        errorDetail.setDetail("Input validation failed");
        errorDetail.setDeveloperMessage(manve.getClass().getName());

        // Create Validation Error instances
        List<FieldError> FieldErrors = manve.getBindingResult().getFieldErrors();
        for(FieldError fe : FieldErrors) {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
            if(validationErrorList == null){
                validationErrorList = new ArrayList<ValidationError>();
                errorDetail.getErrors().put(fe.getField(), validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            // validationError.setMessage(fe.getDefaultMessage());
            validationError.setMessage(messageSource.getMessage(fe,null)); 
            validationErrorList.add(validationError);
        }

        return errorDetail;
    }

}

       /*
        * replaced the handleResourceNotFoundException with handleValidationError
            
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
        */

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Inject
    private MessageSource messageSource;

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            ErrorDetail errorDetail = new ErrorDetail();
            errorDetail.setTimestamp(new Date().getTime()); // set time in milliseconds
            errorDetail.setStatus(status.value());
            errorDetail.setTitle("Message Not Readable");
            errorDetail.setDetail(ex.getMessage());
            errorDetail.setDeveloperMessage(ex.getClass().getName());

            return handleExceptionInternal(ex, errorDetail, headers, status, request);
        }

        /**
         * @param manve MethodArgumentNotValidException
         * @param headers HttpHeaders
         * @param status HttpStatus
         * @param request WebRequest
         * @return
         */
        @Override
        public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException manve, HttpHeaders headers, HttpStatus status, WebRequest request) {
            ErrorDetail errorDetail = new ErrorDetail();
            errorDetail.setTimestamp(new Date().getTime());
            errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
            String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri", 0);

            if(requestPath == null) {
                // requestPath =  request.getRequestURI();
                /*
                 * Use the ServletUriComponentsBuilder: This is a utility class provided by Spring 
                 * that can be used to build URIs based on the current request. You can use the 
                 * fromCurrentRequest() method of the ServletUriComponentsBuilder class to get a 
                 * builder initialized with the current request information, and then use the 
                 * build().toUriString() method to get the request URI as a string.
                 */
                requestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();


            }
            errorDetail.setTitle("Validation Failed");
            errorDetail.setDetail("Input validation failed");
            errorDetail.setDeveloperMessage(manve.getClass().getName());

            // Create Validation Error instances
            List<FieldError> FieldErrors = manve.getBindingResult().getFieldErrors();
            for(FieldError fe : FieldErrors) {
                List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
                if(validationErrorList == null){
                    validationErrorList = new ArrayList<ValidationError>();
                    errorDetail.getErrors().put(fe.getField(), validationErrorList);
                }
                ValidationError validationError = new ValidationError();
                validationError.setCode(fe.getCode());
                // validationError.setMessage(fe.getDefaultMessage());
                validationError.setMessage(messageSource.getMessage(fe,null)); 
                validationErrorList.add(validationError);
            }
            
            return handleExceptionInternal(manve, errorDetail, headers, status, request);
        }

}