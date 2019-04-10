package com.cdtlab19.coffeeapi.services;

import com.cdtlab19.coffeeapi.domain.Identity;
import com.cdtlab19.coffeeapi.domain.StoreEnrollement;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class BlockchainService {

    static final String privateKey_path = "./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec_sk");//getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream("./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec_sk";
    static final String certificate_path = "./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem";

    public BlockchainService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    private Identity loadIdentity() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
        StoreEnrollement enrollment = new StoreEnrollement(loadPrivateKey(privateKey_path), loadCertificate(certificate_path));
        return new Identity("adminorg1", "org1", "Org1MSP", enrollment);
    }


    private PrivateKey loadPrivateKey(String path) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        String privateKeyContent = new String(Files.readAllBytes(Paths.get(path)));

        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyContent);

        KeyFactory kf = KeyFactory.getInstance("ECDSA", "BC");

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        return kf.generatePrivate(spec);
    }

    private String loadCertificate(String path) throws FileNotFoundException, IOException {
        return new String(IOUtils.toByteArray(new FileInputStream(path)), "UTF-8");
    }

    private void connectToFabricNetwork(Identity identity) {
        // configurar toda conexão com o fabric, utilizando o package connection s/ SessionConnection
        // Talvez retornar o ConnectionFabric
    }

    private void connectToChannel() {

    }

//    private void invoke() {
//
//    }
//
//    private void query() {
//
//    }


    /*
            A parte de connection, tirando a SessionConnection, pode ser integrada ao projeto, sem prejuízos, porém sendo verificados.
     */
}
