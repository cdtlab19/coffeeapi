package com.cdtlab19.coffeeapi.services.exceptions;

public class StartConnectionException extends APIException {

    private static final long serialVersionUID = 1L;

    public StartConnectionException(String msg) {
        super(msg, 500);
    }

    public StartConnectionException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}

