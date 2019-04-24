package com.cdtlab19.coffeeapi.services.exceptions;

public class NetworkPeerException extends APIException {

    private static final long serialVersionUID = 1L;

    public NetworkPeerException(String msg) {
        super(msg, 500);
    }

    public NetworkPeerException(String msg, Throwable cause) {
        super(msg, 500, cause);
    }

}
