package com.cdtlab19.coffeeapi.domain;

import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.security.PrivateKey;

public class StoreEnrollement implements Enrollment, Serializable {
    private static final long serialVersionUID = 1L;

    private PrivateKey privateKey;
    private String certificate;

    public StoreEnrollement() { }

    public StoreEnrollement(PrivateKey privateKey, String certificate) {
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    public PrivateKey getKey() {
        return privateKey;
    }

    public String getCert() {
        return certificate;
    }

}
