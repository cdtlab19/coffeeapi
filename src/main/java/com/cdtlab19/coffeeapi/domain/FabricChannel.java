package com.cdtlab19.coffeeapi.domain;

import lombok.NoArgsConstructor;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;

@NoArgsConstructor
public class FabricChannel {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @PreDestroy
    public void clear() {
        LOGGER.info("Clear Channel Fabric");
        try {
            LOGGER.info("Channel initialized {}", channel.isInitialized());
            channel.shutdown(true);
        } catch (Exception e) {
            // TODO: handle exception
            LOGGER.info("Empty channel");
        }
    }

    public void setChannel(HFClient client, NetworkConfig network, String channelName)
            throws InvalidArgumentException, TransactionException, NetworkConfigurationException {

        try {
            LOGGER.info("Channel initialized {}", channel.isInitialized());
            if (channel.isInitialized())
                channel.shutdown(true);
        } catch (Exception e) {
            // TODO: handle exception
            LOGGER.info("Empty channel");
        }

        LOGGER.info("Try from file");
        channel = client.loadChannelFromConfig(channelName, network);
        channel.initialize();

        LOGGER.info("Channel connection {}", channel.getName());
        LOGGER.info("Client context {}", client.getUserContext().getName());
    }
}
