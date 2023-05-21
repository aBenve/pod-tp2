package ar.edu.itba.pod.tp2.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info(" Server Starting ...");

        Config config = new Config();

        //config.getSerializationConfig().addDataSerializableFactory(1, new SerializationFactory() );

        GroupConfig groupConfig = new GroupConfig()
                .setName("benve")
                .setPassword("benve");
        config.setGroupConfig(groupConfig);

        MulticastConfig multicastConfig = new MulticastConfig();
        JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList("192.168.1.*"))
                .setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);

        // Management Center
        //ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig().setUrl("http://localhost:32768/mancenter/").setEnabled(true);
        //config.setManagementCenterConfig(managementCenterConfig);

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
