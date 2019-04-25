package com.cdtlab19.coffeeapi.controllers;

import com.cdtlab19.coffeeapi.dto.IdentityDTO;
import com.cdtlab19.coffeeapi.responses.Response;
import com.cdtlab19.coffeeapi.services.BlockchainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value="/api/wallet")
@Api(value="Título", description="Descrição")
public class WalletController {
    @Autowired
    BlockchainService blockchainService;

    @ApiOperation(value="Descrição")
    @ApiResponses(
            value={
                    @ApiResponse(code=200, message="Sucesso"),
            }
    )
    @PostMapping(path="/")
    public ResponseEntity<List<String>> registerIdentity(IdentityDTO identityModel) throws IOException, TransactionException, InstantiationException, InvocationTargetException, ExecutionException, InterruptedException, IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException, ClassNotFoundException, NoSuchMethodException, ProposalException {
        blockchainService.setPrivateKey_path(identityModel.getPrivateKey());
        blockchainService.setCertificate_path(identityModel.getCertificate());
        blockchainService.setCHANNEL(identityModel.getChannel());
        List<String> response = Arrays.asList(blockchainService.getPrivateKey_path(), blockchainService.getCertificate_path(), blockchainService.getCHANNEL());
        return new ResponseEntity<>(response , HttpStatus.OK );
    }

}
