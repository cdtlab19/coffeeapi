package com.cdtlab19.coffeeapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdentityDTO {
    private String privateKey;
    private String certificate;
    // private String channel;

    public String getPrivateKey() {
        return privateKey;
    }

    public String getCertificate() {
        return certificate;
    }

    // public String getChannel(){return channel; }
}
