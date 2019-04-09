package com.cdtlab19.coffeeapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/wallet")
public class WalletController {

    @PostMapping(path="identity")
    public ResponseEntity<?> loadIdentity() {
        return ResponseEntity.ok().body("Olá");
    }
}
