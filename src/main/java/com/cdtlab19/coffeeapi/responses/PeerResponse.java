package com.cdtlab19.coffeeapi.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class PeerResponse extends Response  {
    private String peer;
    private String status;
    private String message;
    private String id;

    public PeerResponse(String peer, String status, String message, String id) {
        this.status = status;
        this.peer = peer;
        this.message = message;
        this.id = id;
    }

    public PeerResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
