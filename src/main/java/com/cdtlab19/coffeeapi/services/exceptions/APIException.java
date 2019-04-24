package com.cdtlab19.coffeeapi.services.exceptions;

public class APIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer status;

    public APIException (String msg, Integer status, Throwable cause) {
        super(msg, cause);
        this.status = status;
    }

    public APIException (String msg, Integer status) {
        super(msg);
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
