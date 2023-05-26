package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.Collators.OrderStationsByTripsOrAlphabetic;
import ar.edu.itba.pod.tp2.Collators.OrderStationsByVelocityOrAlphabetic;
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
import com.hazelcast.core.DistributedObject;
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
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;


public class Client {

    private static final String OUTPUT_FILE_NAME = "output.csv";
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {


        if(args.length == 0){
            logger.error("Missing query");
            System.exit(1);
        }

        String selectedQuery = args[0];

        // ./queryX -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX-DoutPath=YY [params]
        final String RAWAddresses = ClientUtils.getProperty(InputProperty.ADDRESSES, () -> "Missing addresses").orElse("");
        final String nodesAddresses[] = RAWAddresses.split(";");

        final String inPath = ClientUtils.getProperty(InputProperty.IN_PATH, () -> "Missing inPath").orElse("");
        final String outPath = ClientUtils.getProperty(InputProperty.OUT_PATH, () -> "Missing outPath").orElse("");


        logger.info("Client Starting ...");

        // Client Config
        ClientConfig clientConfig = new ClientConfig();
        // Group Config
        GroupConfig groupConfig = new GroupConfig()
                .setName("i61448")
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

        IMap<Integer, Bike> bikeIMap = hazelcastInstance.getMap("i61448-bike-map");
        IMap<Integer, Station> stationIMap = hazelcastInstance.getMap("i61448-station-map");


        bikeIMap.putAll(readerHelper.getBikesData());
        stationIMap.putAll(readerHelper.getStationsData());

        logger.info("Fin de la lectura del archivo");
        logger.info("Inicio del trabajo map/reduce");

        switch (selectedQuery) {
            case "query1" -> {
                Optional<SortedSet<Map.Entry<String, Integer>>> query1Result = query1(hazelcastInstance, KeyValueSource.fromMap(bikeIMap));

                if (query1Result.isPresent())
                    ClientUtils.writeQuery1(outPath, query1Result.get());
                else
                    logger.error("Error en la ejecucion de la query1");

                System.out.println(query1Result);
            }
            case "query2" -> {
                Optional<SortedSet<Map.Entry<String, SecondQueryOutputData>>> query2Result = query2(hazelcastInstance, KeyValueSource.fromMap(bikeIMap));

                if (query2Result.isPresent())
                    ClientUtils.writeQuery2(outPath, query2Result.get());
                else
                    logger.error("Error en la ejecucion de la query2");

                System.out.println(query2Result);
            }
            default -> {
                logger.error("Invalid query");
                System.exit(1);
            }
        }


        logger.info("Fin del trabajo map/reduce");

        // ----------------------------------------

        // Clean all maps
        bikeIMap.clear();
        stationIMap.clear();
        hazelcastInstance.getDistributedObjects().forEach(DistributedObject::destroy);


        // Shutdown
        HazelcastClient.shutdownAll();
    }

    private static Optional<SortedSet<Map.Entry<String, Integer>>> query1 (HazelcastInstance hazelcastInstance, KeyValueSource<Integer, Bike> bikeKeyValueSource ){
        JobTracker jobTracker = hazelcastInstance.getJobTracker("i61448-query1");
        Job<Integer, Bike> job = jobTracker.newJob(bikeKeyValueSource);

        JobCompletableFuture<SortedSet<Map.Entry<String, Integer>>> future = job
                .mapper(new OnlyMemberBikesMapper())
                .reducer(new CountReducerFactory())
                .submit(new OrderStationsByTripsOrAlphabetic());
        try {
            SortedSet<Map.Entry<String, Integer>> resultMap = future.get();
            return Optional.of(resultMap);
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static Optional<SortedSet<Map.Entry<String, SecondQueryOutputData>>> query2( HazelcastInstance hazelcastInstance, KeyValueSource<Integer, Bike> bikeKeyValueSource ) {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("i61448-query2");
        Job<Integer, Bike> job = jobTracker.newJob(bikeKeyValueSource);

        JobCompletableFuture<SortedSet<Map.Entry<String, SecondQueryOutputData>>> future = job
                .mapper(new StationsNamesWithDatesAndDistanceMapper())
                .reducer(new TakeFastestTravelReducerFactory())
                .submit(new OrderStationsByVelocityOrAlphabetic());

        try {
            SortedSet<Map.Entry<String, SecondQueryOutputData>> resultMap = future.get();
            return Optional.of(resultMap);
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // TODO: REWRITE THIS

    /*
    station;started_trips
    Métro Mont-Royal (Rivard / du Mont-Royal);89836
    Marquette / du Mont-Royal;61421
     */

}
