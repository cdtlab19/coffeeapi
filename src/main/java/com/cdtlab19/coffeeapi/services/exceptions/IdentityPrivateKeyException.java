package com.cdtlab19.coffeeapi.services.exceptions;

public class IdentityPrivateKeyException extends APIException {

    private static final long serialVersionUID = 1L;

    public IdentityPrivateKeyException(String msg) {
        super(msg, 500);
    }

    public IdentityPrivateKeyException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}

