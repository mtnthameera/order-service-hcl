package com.nipun.ecom.orderservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Nipun on 3/6/22
 */

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity handleOrderNotFoundException(OrderNotFoundException ex){

        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiException(NOT_FOUND,ex.getMessage()));
    }

    @ExceptionHandler(NoStocksAvailableException.class)
    public ResponseEntity handleNoStocksAvailableException(NoStocksAvailableException ex){

        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiException(NOT_FOUND,ex.getMessage()));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //return super.handleMethodArgumentNotValid(ex, headers, status, request);
        return ResponseEntity.badRequest()
                .body(ApiException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ex.getBindingResult().getFieldError().toString())
                        .build());
    }
}
