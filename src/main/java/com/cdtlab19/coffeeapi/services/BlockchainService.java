package com.cdtlab19.coffeeapi.services;

import com.cdtlab19.coffeeapi.domain.FabricConnection;
import com.cdtlab19.coffeeapi.domain.FabricNetwork;
import com.cdtlab19.coffeeapi.domain.Identity;
import com.cdtlab19.coffeeapi.domain.StoreEnrollement;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BlockchainService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    static final String privateKey_path = "./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec_sk";//getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream("./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec_sk";
    static final String certificate_path = "./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem";

    public BlockchainService() {
        Security.addProvider(new BouncyCastleProvider());
        LOGGER.info("Security provider added.");
    }

    private Identity loadIdentity() throws IOException {
        LOGGER.info("Loading Identity...");
        try {
            StoreEnrollement enrollment = new StoreEnrollement(loadPrivateKey(privateKey_path), loadCertificate(certificate_path));
            Identity identity = new Identity("adminorg1", "Org1", "Org1MSP", enrollment);
            LOGGER.info("Identity loaded: {}.", identity.getName());

            return identity;
        } catch (IOException e) {
            LOGGER.error("Error to load Identity, returning Identity with null.");
            e.printStackTrace();
            throw e;
        }
    }


    private PrivateKey loadPrivateKey(String path) throws IOException {

        try {
            LOGGER.info("Trying load private key...");
            FileReader fileReader = new FileReader(privateKey_path);
            PEMParser pemParser = new PEMParser(fileReader);
            PrivateKeyInfo pemPair = (PrivateKeyInfo) pemParser.readObject();
            PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
            return privateKey;
        } catch (IOException e) {
            LOGGER.error("Deu erro aqui e nem sei o motivo...");
            e.printStackTrace();
            throw e;
        }
    }

    private String loadCertificate(String path) throws IOException {

        try {
            return new String(IOUtils.toByteArray(new FileInputStream(path)), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private FabricConnection connectToFabricNetwork(Identity identity) throws IOException, NetworkConfigurationException, InvalidArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException {

        FabricConnection fabricConnection = new FabricConnection();
        FabricNetwork fabricNetwork = new FabricNetwork();
        LOGGER.info("fabricConnection and fabricNetwork created.");
        // initialize all fabric configurations, and create a HFClient to communicate with Fabric
        try {
            fabricConnection.initializesConnection();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException |
                 CryptoException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        LOGGER.info("Connection with fabric initialized");
        // set the context to Identity (cert and key) that we'll use, that is, it tell to Fabric our privilege
        fabricConnection.setContext(identity);
        LOGGER.info("set fabricConnection context to Identity {}", identity);
        // Initialize the Fabric network using HFClient created with fabricConnection and Affiliation identity

        try {
            fabricNetwork.initializate(fabricConnection.getConnection(), identity.getAffiliation());
            LOGGER.info("fabricNetwork initialized with {}", identity.getAffiliation());
            return fabricConnection;
        } catch (InvalidArgumentException | NetworkConfigurationException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public FabricConnection testando() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, NetworkConfigurationException, InvalidArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, CryptoException, NoSuchMethodException, ClassNotFoundException {
//        Identity id = loadIdentity();
//        LOGGER.error(id.getEnrollment().getCert());
//        LOGGER.error(String.valueOf(id.getEnrollment().getKey()));
        return connectToFabricNetwork(loadIdentity());
    }

//    private void connectToChannel() {
//
//    }
//
//    private void installChaincode() {
//
//    }
//
//    private void instantiateChaincode() {
//
//    }
//
//    private void upgradeChaincode() {
//
//    }
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
