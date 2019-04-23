package com.cdtlab19.coffeeapi.controllers;

import com.cdtlab19.coffeeapi.domain.Fabric;
import com.cdtlab19.coffeeapi.dto.CoffeeDTO;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value="/api/coffees")
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
    @GetMapping(path="/")
    public ResponseEntity<List<Response>> allCoffee() throws IOException, NoSuchAlgorithmException, InvocationTargetException, InstantiationException, InvalidArgumentException, CryptoException, NoSuchProviderException, IllegalAccessException, NetworkConfigurationException, InvalidKeySpecException, NoSuchMethodException, ClassNotFoundException, InterruptedException, ExecutionException, TransactionException, ProposalException {
//        String[] args ={""};
        List<Response> response = coffeeService.QueryCoffee(new String[] {""}, "AllCoffee");
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PutMapping(path="/{id}/use/{userId}")
    public ResponseEntity<List<Response>> useCoffee(UseCoffeeDTO useCoffeeModel) throws IOException, NoSuchAlgorithmException, InvocationTargetException, InstantiationException, InvalidArgumentException, CryptoException, NoSuchProviderException, IllegalAccessException, NetworkConfigurationException, InvalidKeySpecException, NoSuchMethodException, ClassNotFoundException, InterruptedException, ExecutionException, TransactionException, ProposalException {
        String[] args ={useCoffeeModel.getCoffeeId(),useCoffeeModel.getUserId()};
        List<Response> response = coffeeService.InvokeCoffee(args, "UseCoffee");
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PostMapping(path="/")
    public  ResponseEntity<List<Response>> createCoffee(CoffeeDTO coffeeModel) throws IOException, TransactionException, InstantiationException, InvocationTargetException, ExecutionException, InterruptedException, IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException, ClassNotFoundException, NoSuchMethodException, ProposalException {
        String[] args={coffeeModel.getType()};
        List<Response> response = coffeeService.InvokeCoffee(args, "CreateCoffee");
        return new ResponseEntity<>(response , HttpStatus.OK );
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<List<Response>> updateCoffee(@PathVariable String id, CoffeeDTO coffeeModel) throws IOException, TransactionException, InstantiationException, InvocationTargetException, ExecutionException, InterruptedException, IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException, ClassNotFoundException, NoSuchMethodException, ProposalException {
        String[] args = {coffeeModel.getType()};
        List<Response> response = coffeeService.InvokeCoffee(new String[]{id}, "DeleteCoffee");
        response = coffeeService.InvokeCoffee(args, "CreateCoffee");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping (path="/{id}")
    public  ResponseEntity<List<Response>> GetCoffee(CoffeeDTO coffeemodel, HttpServletRequest request) throws InvalidArgumentException, NoSuchAlgorithmException, IOException, NoSuchProviderException, NetworkConfigurationException, InvalidKeySpecException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, InterruptedException, ExecutionException, TransactionException, ProposalException {
        String[] args = {coffeemodel.getId()};
        List<Response> response = coffeeService.QueryCoffee(args, "GetCoffee");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping (path="/{id}")
    public  ResponseEntity<List<Response>> DeleteCoffee(CoffeeDTO coffeemodel, HttpServletRequest request) throws IOException, TransactionException, InstantiationException, InvocationTargetException, ExecutionException, InterruptedException, IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException, ClassNotFoundException, NoSuchMethodException, ProposalException {
        String[] args = {coffeemodel.getId()};
        List<Response> response = coffeeService.InvokeCoffee(args, "DeleteCoffee");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
