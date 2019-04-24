package com.cdtlab19.coffeeapi.domain;


import com.cdtlab19.coffeeapi.services.exceptions.NetworkConfigException;
import com.cdtlab19.coffeeapi.services.exceptions.NetworkPeerException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FabricNetwork {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final String NETWORK_CONFIG = "./basic-network/connection.yaml";

    private NetworkConfig networkConfig;
    private Collection<Peer> peersOrg;
    private List<String> channels;

    public void initializate(HFClient client, String organization) throws NetworkConfigException, NetworkPeerException {
            // throws InvalidArgumentException, NetworkConfigurationException, IOException {
        peersOrg = new LinkedList<>();
        channels = new ArrayList<>();
        LOGGER.info("Initializes array peers");

        String configFile = NETWORK_CONFIG;

        File file = new File(configFile);
        LOGGER.info("Open file configuration {}", configFile);

        try {
            networkConfig = NetworkConfig.fromYamlFile(file);
        } catch (InvalidArgumentException | IOException | NetworkConfigurationException e) {
            throw new NetworkConfigException("Failed to configure network", e);
        }
        LOGGER.info("Load configuration in NetworkConfig");

        NetworkConfig.OrgInfo organizationInfo = networkConfig.getOrganizationInfo(organization);
        if (organizationInfo == null) {
            throw new NetworkConfigException("organizationInfo is null");
        }
        LOGGER.info("NetworkConfig organization: {}", organizationInfo.getName());

        List<String> peersNames = organizationInfo.getPeerNames();
        for (String itPeer : peersNames) {
            Peer checkPeer = null;
            try {
                checkPeer = client.newPeer(itPeer, networkConfig.getPeerProperties(itPeer).getProperty("url"),	networkConfig.getPeerProperties(itPeer));
            } catch (InvalidArgumentException e) {
                LOGGER.error("Error creating peer");
                throw new NetworkPeerException("Error creating peer", e);
            }

            peersOrg.add(checkPeer);

            LOGGER.info("Peer: {}", itPeer);
        }

        LOGGER.info("Finish initializes");

        for(String channelName : networkConfig.getChannelNames()) {
            channels.add(channelName);
        }

		/*
		for (String orderers : networkConfig.getOrdererNames()) {
			System.out.println(orderers);
			System.out.println(networkConfig.getOrdererProperties(orderers));
		}

		for (String channels : networkConfig.getChannelNames()) {
			System.out.println(channels);
		}
		*/
    }
}
