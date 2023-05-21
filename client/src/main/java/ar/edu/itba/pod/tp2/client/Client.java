package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.client.utils.ClientUtils;
import ar.edu.itba.pod.tp2.client.utils.InputProperty;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import java.nio.file.Files.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {

        // ./queryX -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX-DoutPath=YY [params]
        final String RAWAddresses = ClientUtils.getProperty(InputProperty.ADDRESSES, () -> "Missing addresses").orElse("");
        final String inPath = ClientUtils.getProperty(InputProperty.IN_PATH, () -> "Missing inPath").orElse("");
        final String outPath = ClientUtils.getProperty(InputProperty.OUT_PATH, () -> "Missing outPath").orElse("");

        logger.info("Client Starting ...");

        // Client Config
        ClientConfig clientConfig = new ClientConfig();
        // Group Config
        GroupConfig groupConfig = new GroupConfig()
                .setName("benve")
                .setPassword("benve");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        String[] addresses = {"192.168.1.98:5701"};
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        // Here we create a Hazelcast client and connect to a cluster that has a node
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        // ----------------------------------------
        // Here we do some operations on the cluster


        logger.info("Inicio de la lectura del archivo");


        logger.info("Fin de la lectura del archivo");

        logger.info("Inicio del trabajo map/reduce");

        logger.info("Fin del trabajo map/reduce");

        String mapName = "testMap";
        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);

        testMapFromMember.set(1, "test1");
        IMap<Integer, String> testMap = hazelcastInstance.getMap(mapName);
        System.out.println(testMap.get(1));

        // ----------------------------------------

        // Shutdown
        HazelcastClient.shutdownAll();
    }

    // TODO: REWRITE THIS
    private static void writeStationsDetail(String outPath) {
        StringBuilder answer = new StringBuilder();

        answer.append("--------------------------------------\n");
        createFile(outPath, answer.toString());
    }

    private static void createFile(String outPath, String answer) {
        File outFile = new File(outPath);
        try {
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            writer.write(answer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while creating file");
        }
    }
}
