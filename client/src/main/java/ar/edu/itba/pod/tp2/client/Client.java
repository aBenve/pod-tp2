package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.Mappers.OnlyMemberBikesMapper;
import ar.edu.itba.pod.tp2.Mappers.StationsNamesWithDatesAndDistanceMapper;
import ar.edu.itba.pod.tp2.Models.*;
import ar.edu.itba.pod.tp2.Reducers.CountReducerFactory;
import ar.edu.itba.pod.tp2.Reducers.TakeFastestTravelReducerFactory;
import ar.edu.itba.pod.tp2.client.utils.CSVReaderHelper;
import ar.edu.itba.pod.tp2.client.utils.ClientUtils;
import ar.edu.itba.pod.tp2.client.utils.InputProperty;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

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

                CSVReaderHelper readerHelper = new CSVReaderHelper(inPath, ';');

                IMap<Integer, Bike> bikeIMap = hazelcastInstance.getMap("bike-map");
                IMap<Integer, Station> stationIMap = hazelcastInstance.getMap("station-map");

                bikeIMap.putAll(readerHelper.getBikesData());
                stationIMap.putAll(readerHelper.getStationsData());


        logger.info("Fin de la lectura del archivo");
        logger.info("Inicio del trabajo map/reduce");

                // Query 1
                    JobTracker jobTracker = hazelcastInstance.getJobTracker("query1");
                    KeyValueSource<Integer, Bike> bikeKeyValueSource = KeyValueSource.fromMap(bikeIMap);
                    Job<Integer, Bike> job = jobTracker.newJob(bikeKeyValueSource);

                    JobCompletableFuture<Map<String, Integer>> future = job
                            .mapper(new OnlyMemberBikesMapper())
                            .reducer(new CountReducerFactory())
                            .submit();

                    Map<String, Integer> resultMap = future.get();
                    System.out.println(resultMap);
                // Query 1

                // Query 2
                    Integer N = 5;

                    Job<Integer, Bike> job2 = jobTracker.newJob(bikeKeyValueSource);
                    JobCompletableFuture<Map<String, SecondQueryOutputData>> future2 = job2
                            .mapper(new StationsNamesWithDatesAndDistanceMapper())
                            .reducer(new TakeFastestTravelReducerFactory())
                            .submit();

                    Map<String, SecondQueryOutputData> resultMap2 = future2.get();
                    System.out.println(resultMap2);
                // Query 2

        logger.info("Fin del trabajo map/reduce");

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
