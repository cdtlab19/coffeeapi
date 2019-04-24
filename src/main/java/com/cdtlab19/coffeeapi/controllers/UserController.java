package com.cdtlab19.coffeeapi.controllers;

import com.cdtlab19.coffeeapi.dto.UserDTO;
import com.cdtlab19.coffeeapi.responses.Response;
import com.cdtlab19.coffeeapi.services.UserService;
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

/**
 * UserController
 */
@RestController
@RequestMapping(value="/api/users")
@Api(value="Título", description="Descrição")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value="Descrição")
    @ApiResponses(
            value={
                    @ApiResponse(code=200, message="Sucesso"),
            }
    )

    @PostMapping(path="/")
    public ResponseEntity<List<Response>> createUser(UserDTO user)throws IOException, TransactionException,
            InstantiationException, InvocationTargetException, ExecutionException, InterruptedException,
            IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException,
            ClassNotFoundException, NoSuchMethodException, ProposalException {

        // String[] args={user.getName(), user.getRemainingCoffee().toString()};
        String[] args={user.getName()};
        List<Response> response = userService.InvokeUser(args, "CreateUser");
        return new ResponseEntity<>(response , HttpStatus.OK );
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<List<Response>> updateUser(@PathVariable String id, UserDTO user) throws IOException, TransactionException, InstantiationException, InvocationTargetException, ExecutionException, InterruptedException, IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException, ClassNotFoundException, NoSuchMethodException, ProposalException {
        String[] args = {user.getName(),user.getRemainingCoffee().toString()};
        List<Response> response = userService.InvokeUser(new String[]{id}, "DeleteUser");
        response = userService.InvokeUser(args, "CreateUser");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping (path="/{id}")
    public  ResponseEntity<List<Response>> deleteUser(@PathVariable String id) throws IOException, TransactionException, InstantiationException, InvocationTargetException, ExecutionException, InterruptedException, IllegalAccessException, InvalidArgumentException, NetworkConfigurationException, CryptoException, ClassNotFoundException, NoSuchMethodException, ProposalException {
        List<Response> response = userService.InvokeUser(new String[]{id}, "DeleteUser");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public  ResponseEntity<List<Response>> getUser(@PathVariable String id) throws InvalidArgumentException, NoSuchAlgorithmException, IOException, NoSuchProviderException, NetworkConfigurationException, InvalidKeySpecException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, InterruptedException, ExecutionException, TransactionException, ProposalException {
        List<Response> response = userService.QueryUser(new String[]{id}, "GetUser");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/")
    public ResponseEntity<Response> allUsers() throws IOException, NoSuchAlgorithmException, InvocationTargetException, InstantiationException, InvalidArgumentException, CryptoException, NoSuchProviderException, IllegalAccessException, NetworkConfigurationException, InvalidKeySpecException, NoSuchMethodException, ClassNotFoundException, InterruptedException, ExecutionException, TransactionException, ProposalException{
        List<Response> response = userService.QueryUser(new String[] {""}, "AllUser");
        return new ResponseEntity<>(response.get(0) , HttpStatus.OK);

    }


}
