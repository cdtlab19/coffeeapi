package com.cdtlab19.coffeeapi.controllers.exceptions;

import com.cdtlab19.coffeeapi.services.exceptions.APIException;
import com.cdtlab19.coffeeapi.services.exceptions.ChannelException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@ControllerAdvice
public class ControllerExceptionHandler {

    // AJEITAR OS OS CÓDIGOS HTTP

    @ExceptionHandler(APIException.class)
    public ResponseEntity<StandardError> exceptions(APIException e, HttpServletRequest request) {
        StandardError err = new StandardError(e.getStatus(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(e.getStatus()).body(err);
    }

}








//:^)
//
//
//public class APIException extends RuntimeException {
//
//    private int status
//
//    public int GetStatus() {
//        return status;
//    }
//
//    public void SetStatus(int status) {
//        this.status = status;
//    }
//
//    public APIException(int status, String msg) {
//        super(msg);
//        this.status = status;
//    }
//}
//
//public class FabricException extends APIException {
//
//}
//
//public class FabricNotFoundException extends FabricException {
//    public FabricNotFoundException(String message) {
//        super(404, message)
//    }
//}
//
//public class FabricQueryNotFoundException extends FabricException {
//    public FabricNotFoundException(String message) {
//        super(404, message)
//    }
//}
//public class FabricErroFudidoException extends FabricException {
//    public FabricNotFoundException(String message) {
//        super(500, message)
//    }
//}
//
//GetStatus()
//    GetMessage()
//
//public class ErrorResponse implements Serializable {
//    public String error
//
//    public ErrorResponse(String message) {
//        this.error = message
//    }
//}