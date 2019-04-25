package com.cdtlab19.coffeeapi.services;

import com.cdtlab19.coffeeapi.domain.*;
import com.cdtlab19.coffeeapi.responses.PeerResponse;
import com.cdtlab19.coffeeapi.responses.QueryResponse;
import com.cdtlab19.coffeeapi.responses.Response;
import com.cdtlab19.coffeeapi.responses.SdkResponse;
import com.cdtlab19.coffeeapi.services.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class BlockchainService {

    private ObjectMapper mapper = new ObjectMapper();
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String privateKey_path;
    private String certificate_path;
    private String CHANNEL;
//    static final String privateKey_path = "./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec_sk";
//    static final String certificate_path = "./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem";
//    static final String CHANNEL = "mychannel";

    public BlockchainService() {
        Security.addProvider(new BouncyCastleProvider());
        LOGGER.info("Security provider added.");
    }


    private Identity loadIdentity() throws IdentityPrivateKeyException, IdentityCertificateException {
        LOGGER.info("Loading Identity...");

        StoreEnrollement enrollment = new StoreEnrollement(loadPrivateKey(privateKey_path), loadCertificate(certificate_path));
        Identity identity = new Identity("adminorg1", "Org1", "Org1MSP", enrollment);
        LOGGER.info("Identity loaded: {}.", identity.getName());

        return identity;
    }


    private PrivateKey loadPrivateKey(String path) throws IdentityPrivateKeyException {
        try {
            FileReader fileReader = new FileReader(privateKey_path);
            PEMParser pemParser = new PEMParser(fileReader);

            PrivateKeyInfo pemPair = (PrivateKeyInfo) pemParser.readObject();

            PrivateKey privateKey = new JcaPEMKeyConverter()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .getPrivateKey(pemPair);

            return privateKey;
        } catch (IOException e) {
            LOGGER.error("failed reading private key");
            throw new IdentityPrivateKeyException("failed reading private key", e);
        }
    }

    private String loadCertificate(String path) throws IdentityCertificateException {
        LOGGER.info("Loading certificate.");

        try {
            return new String(IOUtils.toByteArray(new FileInputStream(path)), "UTF-8");
        } catch (IOException e) {
            LOGGER.error("failed reading certificate");
            throw new IdentityCertificateException("failed reading certificate", e);
        }

    }

    private Fabric connectToFabricNetwork(Identity identity) {

        FabricConnection fabricConnection = new FabricConnection();
        FabricNetwork fabricNetwork = new FabricNetwork();
        LOGGER.info("fabricConnection and fabricNetwork created.");
        // initialize all fabric configurations, and create a HFClient to communicate with Fabric

        fabricConnection.initializesConnection();

        LOGGER.info("Connection with fabric initialized");

        // set the context to Identity (cert and key) that we'll use, that is, it tell to Fabric our privilege
        fabricConnection.setContext(identity);
        LOGGER.info("set fabricConnection context to Identity {}", identity);
        // Initialize the Fabric network using HFClient created with fabricConnection and Affiliation identity

        fabricNetwork.initializate(fabricConnection.getConnection(), identity.getAffiliation());

        LOGGER.info("fabricNetwork initialized with {}", identity.getAffiliation());
        return new Fabric(fabricConnection, fabricNetwork);

    }

    public List<Response> invoke(String chaincodeName, String chaincodeMethod, String path, String[] arguments)
            throws InvokeException, TransactionException {

        Fabric fabricTmp = connectToFabricNetwork(loadIdentity());

        HFClient client = fabricTmp.getFabricConnection().getConnection();
        FabricNetwork network = fabricTmp.getFabricNetwork();

        Channel channel = loadChannelFromConfig(client, network.getNetworkConfig(), CHANNEL);

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
        LOGGER.info("Channel {}", channel.getName());
        LOGGER.info("Status {}", channel.isInitialized());

        Collection<ProposalResponse> proposalResponses;
        try {
            proposalResponses = channel.sendTransactionProposal(transactionProposalRequest);
        } catch (ProposalException | InvalidArgumentException e) {
            e.printStackTrace();
            throw new InvokeException("Failed invoking chaincode on sendTransactionProposal", e);
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

        return sendTransactionSync(responseInvokeChaincode, channel, proposalResponses);
    }


    private List<Response> sendTransactionSync(List<Response> responseProposal, Channel channel,
                                               Collection<ProposalResponse> proposalResponses) throws TransactionException {

        try {
            BlockEvent.TransactionEvent transactionEvent = channel.sendTransaction(proposalResponses).get();
            LOGGER.info("Size: {}", transactionEvent.getTransactionActionInfoCount());
            for (BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo transaction : transactionEvent.getTransactionActionInfos()) {
                LOGGER.info("Status: {}", transaction.getResponseStatus());
                LOGGER.info("Message: {}", transaction.getResponseMessage());
                LOGGER.info("Payload: {}", new String(transaction.getEvent().getPayload()), StandardCharsets.UTF_8);
                LOGGER.info("Status final: {}", new String(transaction.getProposalResponsePayload(), StandardCharsets.UTF_8));

                responseProposal.add(new SdkResponse(String.valueOf(transaction.getResponseStatus()),
                        transaction.getResponseMessage(),
                        new String(transaction.getEvent().getPayload(), StandardCharsets.UTF_8),
                        new String(transaction.getProposalResponsePayload(), StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
        }

        return responseProposal;
    }

    public List<Response> query(String chaincodeName, String chaincodeMethod, String path, String[] arguments)
            throws QueryException {

        Fabric fabricTmp = connectToFabricNetwork(loadIdentity());
        HFClient client = fabricTmp.getFabricConnection().getConnection();
        FabricNetwork network = fabricTmp.getFabricNetwork();

        Channel channel = loadChannelFromConfig(client, network.getNetworkConfig(), CHANNEL);

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

        Collection<ProposalResponse> response = null;
        try {
            response = channel.queryByChaincode(queryByChaincodeRequest);
        } catch (InvalidArgumentException e) {
            LOGGER.error("Invalid argument sent to queryByChaincode");
            e.printStackTrace();
            throw new QueryException("invalid chaincode request");
        } catch (ProposalException e) {
            LOGGER.error("Invalid chaincode proposal");
            e.printStackTrace();
            throw new QueryException("invalid chaincode proposal");
        }

        LOGGER.info("Reading all responses");
        List<Response> responseQueryTest = new ArrayList<>();

        for (ProposalResponse proposalResponse : response) {
            LOGGER.info("proposalResponse.getStatus() = {}", proposalResponse.getStatus());
            LOGGER.info("proposalResponse.getMessage() = {}", proposalResponse.getMessage());

            String responseKey;

            // status = ERROR
            if (proposalResponse.isInvalid()) {
                throw new QueryException(proposalResponse.getMessage());
            }

            try {
                responseKey = new String(proposalResponse.getChaincodeActionResponsePayload());
            } catch (InvalidArgumentException e) {
                LOGGER.error("invalid argument sent to getChaincodeActionResponsePayload");

                e.printStackTrace();
                throw new QueryException("invalid argument sent to getChaincodeActionResponsePayload");
            }

            PeerResponse peerResponse = new PeerResponse(
                    proposalResponse.getPeer().getName(),
                    proposalResponse.getStatus().toString(),
                    proposalResponse.getMessage(),
                    proposalResponse.getTransactionID());

            try {
                responseQueryTest.add(new QueryResponse(peerResponse, mapper.readValue(responseKey, Object.class)));
            } catch (IOException e) {
                LOGGER.error("failed to read response key value from mapper");
                e.printStackTrace();
                throw new QueryException("failed to read response key value from mapper");
            }
        }

        return responseQueryTest;

    }

    public Channel loadChannelFromConfig(HFClient client, NetworkConfig network, String channelName)
            throws ChannelException {
        Channel channel = null;
        try {
            channel = client.loadChannelFromConfig(channelName, network);
            channel.initialize();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            LOGGER.error("invalid arguments at channel creation");
            throw new ChannelException("Invalid arguments at channel creation: " + e.getMessage());
        } catch (NetworkConfigurationException e) {
            e.printStackTrace();
            throw new ChannelException("Fatal network error: " + e.getMessage());
        } catch (TransactionException e) {
            e.printStackTrace();
            throw new ChannelException("Transaction error: " + e.getMessage());
        }

        if (channel == null) {
            throw new ChannelException("channel is null");
        }

        return channel;
    }

    public void setPrivateKey_path(String privateKey_path) {
        this.privateKey_path = privateKey_path;
    }

    public String getPrivateKey_path() {
        return privateKey_path;
    }

    public String getCertificate_path() {
        return certificate_path;
    }

    public void setCertificate_path(String certificate_path) {
        this.certificate_path = certificate_path;
    }

    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

}


























