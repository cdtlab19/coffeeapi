package com.cdtlab19.coffeeapi.services.exceptions;

public class IdentityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IdentityException(String msg) {
        super(msg);
    }

    public IdentityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
