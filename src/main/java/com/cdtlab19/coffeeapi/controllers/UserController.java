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
    public ResponseEntity<List<Response>> createUser(UserDTO user) throws TransactionException {
        String[] args={user.getName()};
        List<Response> response = userService.InvokeUser(args, "CreateUser");
        return new ResponseEntity<>(response, HttpStatus.CREATED );
    }

    @DeleteMapping (path="/{id}")
    public  ResponseEntity<List<Response>> deleteUser(@PathVariable String id) throws TransactionException {
        List<Response> response = userService.InvokeUser(new String[]{id}, "DeleteUser");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping(path="/{id}")
    public  ResponseEntity<List<Response>> getUser(@PathVariable String id) {
        List<Response> response = userService.QueryUser(new String[]{id}, "GetUser");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/")
    public ResponseEntity<List<Response>> allUsers() {
        List<Response> response = userService.QueryUser(new String[] {""}, "AllUser");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
