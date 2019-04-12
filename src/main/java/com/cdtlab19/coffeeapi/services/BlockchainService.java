package com.cdtlab19.coffeeapi.services;

import com.cdtlab19.coffeeapi.domain.*;
import com.cdtlab19.coffeeapi.domain.Identity;
import com.cdtlab19.coffeeapi.responses.PeerResponse;
import com.cdtlab19.coffeeapi.responses.QueryResponse;
import com.cdtlab19.coffeeapi.responses.Response;
import com.cdtlab19.coffeeapi.responses.SdkResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BlockchainService {

    private ObjectMapper mapper = new ObjectMapper();
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
            LOGGER.error("Error to load Identity.");
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
            LOGGER.info("Private key loaded.");
            return privateKey;
        } catch (IOException e) {
            LOGGER.error("Deu erro aqui e nem sei o motivo...");
            e.printStackTrace();
            throw e;
        }
    }

    private String loadCertificate(String path) throws IOException {

        try {
            LOGGER.info("Loading certificate.");
            return new String(IOUtils.toByteArray(new FileInputStream(path)), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Fabric connectToFabricNetwork(Identity identity) throws IOException, NetworkConfigurationException, InvalidArgumentException,
            IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException {

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
            return new Fabric(fabricConnection, fabricNetwork);
        } catch (InvalidArgumentException | NetworkConfigurationException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Fabric testando() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, NetworkConfigurationException,
            InvalidArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, CryptoException, NoSuchMethodException,
            ClassNotFoundException {
        return connectToFabricNetwork(loadIdentity());
    }

    public List<Response> invoke(String chaincodeName, String chaincodeMethod, String path, String[] arguments) throws IOException, IllegalAccessException,
            InvocationTargetException, InvalidArgumentException, InstantiationException, CryptoException, NoSuchMethodException,
            NetworkConfigurationException, ClassNotFoundException, ProposalException, ExecutionException, InterruptedException, TransactionException {

        Fabric fabricTmp = connectToFabricNetwork(loadIdentity());
        FabricChannel fabricChannel = new FabricChannel();
        HFClient client = fabricTmp.getFabricConnection().getConnection();
        fabricChannel.setChannel(client, fabricTmp.getFabricNetwork().getNetworkConfig(),
                "mychannel");

        // ver depois
        List<Response> responseInvokeChaincode = new ArrayList<>();

        LOGGER.info("Start invokeTest chaincode");
        LOGGER.info("Create TransactionProposalRequest");
        // LOGGER.info("Object {}", chaincode.toString());
        LOGGER.info("ChaincodeName {}", chaincodeName);

        TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName(chaincodeName).build();
        transactionProposalRequest.setChaincodeID(cid);
        transactionProposalRequest.setFcn(chaincodeMethod);
        transactionProposalRequest.setArgs(arguments);

        LOGGER.info("Infos");
        LOGGER.info("Name: {}", transactionProposalRequest.getChaincodeName());
        LOGGER.info("Version: {}", transactionProposalRequest.getChaincodeID().getVersion());
        LOGGER.info("Fcn: {}", transactionProposalRequest.getFcn());
        for (String args : transactionProposalRequest.getArgs())
            LOGGER.info("Args: {}", args);
        LOGGER.info("Channel {}", fabricChannel.getChannel().getName());
        LOGGER.info("Status {}", fabricChannel.getChannel().isInitialized());

        Collection<ProposalResponse> proposalResponses;
        try {
            proposalResponses = fabricChannel.getChannel().sendTransactionProposal(transactionProposalRequest);
        } catch (ProposalException e) {
            e.printStackTrace();
            throw e;
        }
        LOGGER.info("Get response");
        for (ProposalResponse response : proposalResponses) {
            LOGGER.info("Peer {}", response.getPeer().getName());
            LOGGER.info("Status {}", response.getStatus());
            LOGGER.info("Message {}", response.getMessage());
            LOGGER.info("Transaction Id {}", response.getTransactionID());

            responseInvokeChaincode.add(new PeerResponse(response.getPeer().getName(), response.getStatus().toString(),
                    response.getMessage(), response.getTransactionID()));
        }

        LOGGER.info("Send Transaction");

        return sendTransactionSync(responseInvokeChaincode, fabricChannel.getChannel(), proposalResponses);
    }

    private List<Response> sendTransactionSync(List<Response> responseProposal, Channel channel, Collection<ProposalResponse> proposalResponses) {
        try {
            BlockEvent.TransactionEvent transactionEvent = channel.sendTransaction(proposalResponses).get();
            LOGGER.info("Size: {}", transactionEvent.getTransactionActionInfoCount());
            for (BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo transaction : transactionEvent.getTransactionActionInfos()) {
                LOGGER.info("Status: {}", transaction.getResponseStatus());
                LOGGER.info("Message: {}", transaction.getResponseMessage());
                LOGGER.info("Payload: {}", new String(transaction.getEvent().getPayload()), StandardCharsets.UTF_8);
                LOGGER.info("Status final: {}",	new String(transaction.getProposalResponsePayload(), StandardCharsets.UTF_8));

                responseProposal.add(new SdkResponse(String.valueOf(transaction.getResponseStatus()),
                        transaction.getResponseMessage(),
                        new String(transaction.getEvent().getPayload(), StandardCharsets.UTF_8),
                        new String(transaction.getProposalResponsePayload(), StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return responseProposal;
    }

    public List<Response> query(String chaincodeName, String chaincodeMethod, String path, String[] arguments) throws
            IOException, IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException,
            CryptoException, NoSuchMethodException, NetworkConfigurationException, ClassNotFoundException, ProposalException, TransactionException {

        List<Response> responseQueryTest = new ArrayList<>();
        PeerResponse responsePeer;
        String stringResponse = "";


        // establishes a fabric connection
        // then we have its client and channel
        //
        Fabric fabricTmp = connectToFabricNetwork(loadIdentity());
        FabricChannel fabricChannel = new FabricChannel();
        HFClient client = fabricTmp.getFabricConnection().getConnection();
        fabricChannel.setChannel(client, fabricTmp.getFabricNetwork().getNetworkConfig(), "mychannel");

        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName(chaincodeName).build();
        queryByChaincodeRequest.setChaincodeID(cid);
        queryByChaincodeRequest.setFcn(chaincodeMethod);
        queryByChaincodeRequest.setArgs(arguments);

        LOGGER.info("Infos");
        LOGGER.info("Name: {}", queryByChaincodeRequest.getChaincodeName());
        LOGGER.info("Version: {}", queryByChaincodeRequest.getChaincodeID().getVersion());
        LOGGER.info("Fcn: {}", queryByChaincodeRequest.getFcn());
        for (String args : queryByChaincodeRequest.getArgs())
            LOGGER.info("Args: {}", args);

        Collection<ProposalResponse> response = fabricChannel.getChannel().queryByChaincode(queryByChaincodeRequest);
        LOGGER.info("Get response");
        for (ProposalResponse proposalResponse : response) {
            LOGGER.info("status {}", proposalResponse.getStatus());
            LOGGER.info("message {}", proposalResponse.getMessage());

            stringResponse = new String(proposalResponse.getChaincodeActionResponsePayload());
            LOGGER.info("Response {}", stringResponse);
            responsePeer = new PeerResponse(proposalResponse.getPeer().getName(),
                    proposalResponse.getStatus().toString(), proposalResponse.getMessage(),
                    proposalResponse.getTransactionID());

            responseQueryTest.add(new QueryResponse(responsePeer, mapper.readValue(stringResponse, Object.class)));

            //LOGGER.info("Response: {}", stringResponse);
        }

        return responseQueryTest;

    }


    /*
            github.com/cdtlab19/coffee-chaincodes/entry/coffee
            github.com/cdtlab19/coffee-chaincodes/entry/user
            A parte de connection, tirando a SessionConnection, pode ser integrada ao projeto, sem prejuízos, porém sendo verificados.
            Error:(134, 33) java: unreported exception org.hyperledger.fabric.sdk.exception.TransactionException; must be caught or declared to be thrown
     */
}
