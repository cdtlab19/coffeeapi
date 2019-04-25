package com.cdtlab19.coffeeapi.connection;

import org.hyperledger.fabric.sdk.HFClient;

import com.cdtlab19.coffeeapi.domain.Identity;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;


public class FabricConnection {
    private HFClient client;

    public HFClient getConnection() {
        return client;
    }

    public void setContext(Identity identity) throws InvalidArgumentException {
        try {
            client.setUserContext(identity);
        } catch (InvalidArgumentException e) {
            throw e;
        }
    }

}
