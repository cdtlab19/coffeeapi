package com.cdtlab19.coffeeapi.services.exceptions;

public class FabricException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FabricException(String msg) {
        super(msg);
    }

    public FabricException(String msg, Throwable cause) {
        super(msg, cause);
    }
}


