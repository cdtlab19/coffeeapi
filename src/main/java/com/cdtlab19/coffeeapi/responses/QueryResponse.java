package com.cdtlab19.coffeeapi.responses;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryResponse extends Response{
    private PeerResponse peerResponse;
    private Object queryResponse;
}
