package com.cdtlab19.coffeeapi.services.exceptions;

public class QueryException extends APIException {

    private static final long serialVersionUID = 1L;

    public QueryException(String msg) {
        super(msg, 500);
    }

    public QueryException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}
