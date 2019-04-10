package com.cdtlab19.coffeeapi.controllers;

import com.cdtlab19.coffeeapi.domain.FabricConnection;
import com.cdtlab19.coffeeapi.services.BlockchainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;


@RestController
@RequestMapping(value="/api/wallet")
@Api(value="Título", description="Descrição")
public class WalletController {

    @Autowired
    BlockchainService service;

    @ApiOperation(value="Descrição")
    @ApiResponses(
        value={
            @ApiResponse(code=200, message="Sucesso"),
        }
    )
    @PostMapping(path="identity")
    public ResponseEntity<String> loadIdentity() throws InvalidArgumentException, NoSuchAlgorithmException, IOException, NoSuchProviderException, NetworkConfigurationException, InvalidKeySpecException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException {
        FabricConnection fbConnection = service.testando();
        return new ResponseEntity<>(fbConnection.getConnection().getUserContext().getName(), HttpStatus.OK);
    }
}
