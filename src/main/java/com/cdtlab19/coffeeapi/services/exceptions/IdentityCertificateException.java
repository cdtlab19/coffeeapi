package com.cdtlab19.coffeeapi.services.exceptions;

public class IdentityCertificateException extends APIException {

    private static final long serialVersionUID = 1L;

    public IdentityCertificateException(String msg) {
        super(msg, 500);
    }

    public IdentityCertificateException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}

