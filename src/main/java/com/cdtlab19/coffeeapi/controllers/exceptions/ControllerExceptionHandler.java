package com.cdtlab19.coffeeapi.controllers.exceptions;

import com.cdtlab19.coffeeapi.services.exceptions.FabricException;
import com.cdtlab19.coffeeapi.services.exceptions.IdentityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    // AJEITAR OS OS CÓDIGOS HTTP

    @ExceptionHandler(IdentityException.class)
    public ResponseEntity<StandardError> identityExceptions(IdentityException e, HttpServletRequest request) {
        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value() , e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(FabricException.class)
    public ResponseEntity<StandardError> identityExceptions(FabricException e, HttpServletRequest request) {
        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value() , e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

}
