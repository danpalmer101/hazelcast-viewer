package io.danpalmer101.hazelcastviewer.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Configuration of HazelcastInstance
 */
@Configuration
@EnableAutoConfiguration(exclude={HazelcastAutoConfiguration.class})
public class HazelcastConfiguration {

    @Value("${hazelcast.client.instance.name}")
    private String hazelcastClientInstance;

    @Value("${hazelcast.server.addresses}")
    private String hazelcastAddresses;

    /**
     * Programatically create a Hazelcast instance with client config
     * @return a Hazelcast instance
     */
    @Bean
    public HazelcastInstance hazelcastInstance() {
        List<String> hazelcastAddressList = hazelcastAddresses != null
                ? Arrays.asList(hazelcastAddresses.split(","))
                : Collections.<String>emptyList();

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName(hazelcastClientInstance);
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();

        networkConfig.setAddresses(hazelcastAddressList);

        networkConfig.setSmartRouting(true);
        networkConfig.setRedoOperation(true);

        // Instantiate a Hazelcast client with the configuration
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}
