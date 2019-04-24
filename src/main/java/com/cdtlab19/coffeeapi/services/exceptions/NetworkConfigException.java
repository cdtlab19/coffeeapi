package com.cdtlab19.coffeeapi.services.exceptions;

public class NetworkConfigException extends APIException {

    private static final long serialVersionUID = 1L;

    public NetworkConfigException(String msg) {
        super(msg, 500);
    }

    public NetworkConfigException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}

