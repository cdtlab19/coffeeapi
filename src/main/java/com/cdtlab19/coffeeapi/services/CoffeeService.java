package com.cdtlab19.coffeeapi.services;

import com.cdtlab19.coffeeapi.responses.Response;
import org.hyperledger.fabric.sdk.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CoffeeService {

    @Autowired
    BlockchainService service;

    public List<Response> QueryCoffee(String [] args, String method) throws IOException, ExecutionException, InstantiationException, InvocationTargetException, NoSuchMethodException, InterruptedException, IllegalAccessException, InvalidArgumentException, TransactionException, CryptoException, ClassNotFoundException, NetworkConfigurationException, ProposalException {
        return service.query("coffee", method, "github.com/cdtlab19/coffee-chaincode/entry/coffee", args);

    }

    public List<Response> InvokeCoffee(String[] args, String method) throws IOException, ExecutionException, InstantiationException, InvocationTargetException, NoSuchMethodException, InterruptedException, IllegalAccessException, InvalidArgumentException, TransactionException, CryptoException, ClassNotFoundException, NetworkConfigurationException, ProposalException {
        return service.invoke("coffee", method, "github.com/cdtlab19/coffee-chaincode/entry/coffee", args);

    }

    public BlockchainService getService() {
        return service;
    }

}
