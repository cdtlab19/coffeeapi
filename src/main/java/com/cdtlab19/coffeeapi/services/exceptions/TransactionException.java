package com.cdtlab19.coffeeapi.services.exceptions;

public class TransactionException extends APIException {

    private static final long serialVersionUID = 1L;

    public TransactionException(String msg) {
        super(msg, 500);
    }

    public TransactionException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}
