package com.cdtlab19.coffeeapi.controllers;

import com.cdtlab19.coffeeapi.domain.Fabric;
import com.cdtlab19.coffeeapi.dto.UseCoffeeDTO;
import com.cdtlab19.coffeeapi.responses.Response;
import com.cdtlab19.coffeeapi.services.CoffeeService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value="/api/coffee")
@Api(value="Título", description="Descrição")
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    @ApiOperation(value="Descrição")
    @ApiResponses(
            value={
                    @ApiResponse(code=200, message="Sucesso"),
            }
    )
    @GetMapping(path="coffees")
    public ResponseEntity<List<Response>> allCoffee() throws IOException, NoSuchAlgorithmException, InvocationTargetException, InstantiationException, InvalidArgumentException, CryptoException, NoSuchProviderException, IllegalAccessException, NetworkConfigurationException, InvalidKeySpecException, NoSuchMethodException, ClassNotFoundException, InterruptedException, ExecutionException, TransactionException, ProposalException {
        String[] args ={""};
        List<Response> response = coffeeService.QueryCoffee(args, "AllCoffee");
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping(path="/coffees/{id}/use{userId}")
    public ResponseEntity<List<Response>> useCoffee(UseCoffeeDTO useCoffeeModel) throws IOException, NoSuchAlgorithmException, InvocationTargetException, InstantiationException, InvalidArgumentException, CryptoException, NoSuchProviderException, IllegalAccessException, NetworkConfigurationException, InvalidKeySpecException, NoSuchMethodException, ClassNotFoundException, InterruptedException, ExecutionException, TransactionException, ProposalException {
        String[] args ={useCoffeeModel.getCoffeeId(),useCoffeeModel.getUserId()};
        List<Response> response = coffeeService.InvokeCoffee(args, "UseCoffee");
        return new ResponseEntity<>(response , HttpStatus.OK);
    }
}
