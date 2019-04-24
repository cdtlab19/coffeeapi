package com.cdtlab19.coffeeapi.domain;


import com.cdtlab19.coffeeapi.services.exceptions.SetContextException;
import com.cdtlab19.coffeeapi.services.exceptions.StartConnectionException;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor
public class FabricConnection {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private HFClient client;

    public void initializesConnection() throws StartConnectionException {
            //throws IllegalAccessException, InvocationTargetException, InvalidArgumentException,
            //InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
        CryptoSuite cryptoSuite;

        try {
            cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
            LOGGER.info("Get CryptoSuite");

            client = HFClient.createNewInstance();
            client.setCryptoSuite(cryptoSuite);
            LOGGER.info("Use CryptoSuite");
            LOGGER.info("Initializes HFClient");

        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | CryptoException
                | InvalidArgumentException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error("Error initialize HFCLient");
            throw new StartConnectionException("Error initialize HFCLient", e);
        }

    }

    public HFClient getConnection() {
        LOGGER.info("Return client {}", client.getUserContext().getName());
        return client;
    }

    public void setContext(Identity identity) throws SetContextException {
        try {
            client.setUserContext(identity);
            LOGGER.info("Change context client to {}", identity.getAffiliation());
        } catch (InvalidArgumentException e) {
            throw new SetContextException("Error setting context HFClient");
        }
    }
}
