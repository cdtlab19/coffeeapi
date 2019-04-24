package com.cdtlab19.coffeeapi.services.exceptions;

public class SetContextException extends APIException {

    private static final long serialVersionUID = 1L;

    public SetContextException(String msg) {
        super(msg, 500);
    }

    public SetContextException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}

