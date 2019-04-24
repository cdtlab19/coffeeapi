package com.cdtlab19.coffeeapi.services.exceptions;

public class ChannelException extends APIException {

    private static final long serialVersionUID = 1L;

    public ChannelException(String msg) {
        super(msg, 500);
    }

    public ChannelException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}
