package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.Collators.OrderStationsByTripsOrAlphabetic;
import ar.edu.itba.pod.tp2.Collators.OrderStationsByVelocityOrAlphabetic;
import ar.edu.itba.pod.tp2.Mappers.OnlyMemberBikesMapper;
import ar.edu.itba.pod.tp2.Mappers.StationsNamesWithDatesAndDistanceMapper;
import ar.edu.itba.pod.tp2.Models.*;
import ar.edu.itba.pod.tp2.Reducers.CountReducerFactory;
import ar.edu.itba.pod.tp2.Reducers.TakeFastestTravelReducerFactory;
import ar.edu.itba.pod.tp2.client.utils.*;
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
import java.util.*;
import java.util.concurrent.ExecutionException;


public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {


        if(args.length == 0){
            logger.error("Falta la query");
            System.exit(1);
        }

        String selectedQuery = args[0];
        Integer stationsAmount = 0;

        if(selectedQuery.equals("query2")) {
            stationsAmount = Integer.parseInt(ClientUtils.getProperty(InputProperty.AMOUNT_OF_STATIONS, () -> "Falta la cantidad de estaciones").orElse(""));
        }

        // ./queryX -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX-DoutPath=YY [params]
        final String RAWAddresses = ClientUtils.getProperty(InputProperty.ADDRESSES, () -> "Faltan las direcciones de los nodos").orElse("");
        final String nodesAddresses[] = RAWAddresses.split(";");

        if(nodesAddresses.length == 0){
            logger.error("Faltan las direcciones de los nodos");
            System.exit(1);
        }

        final String inPath = ClientUtils.getProperty(InputProperty.IN_PATH, () -> "Falta la direccion de entrada de datos").orElse("");
        final String outPath = ClientUtils.getProperty(InputProperty.OUT_PATH, () -> "Falta la direccion de salida de datos").orElse("");

        logger.info("Iniciando cliente ...");

        ClientConfig clientConfig = new ClientConfig();
        GroupConfig groupConfig = new GroupConfig()
                .setName("i61448")
                .setPassword("benve");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();

        String[] addresses = {"192.168.1.98:5701"};

        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        // ----------------------------------------

        logger.info("Inicio de la lectura del archivo");

        CSVReaderHelper readerHelper = new CSVReaderHelper(inPath, ';');

        IMap<Integer, Bike> bikeIMap = hazelcastInstance.getMap("i61448-bike-map");
        IMap<Integer, Station> stationIMap = hazelcastInstance.getMap("i61448-station-map");

        try{
            bikeIMap.putAll(readerHelper.getBikesData());
            stationIMap.putAll(readerHelper.getStationsData());
        }catch (Exception e){
            logger.error("Error en la lectura del archivo");
            clearAndExit(bikeIMap, stationIMap, hazelcastInstance, true);
        }

        logger.info("Fin de la lectura del archivo");
        logger.info("Inicio del trabajo map/reduce");

        switch (selectedQuery) {
            case "query1" -> {
                Optional<List<Map.Entry<String, Integer>>> query1Result =
                        new FirstQueryResolver("i61448-query1", hazelcastInstance, bikeIMap)
                                .resolve();

                if (query1Result.isPresent())
                    ClientUtils.writeQuery1(outPath, query1Result.get());
                else
                {
                    logger.error("Error en la ejecucion de la query1");
                    clearAndExit(bikeIMap, stationIMap, hazelcastInstance, true);
                }
            }
            case "query2" -> {
                Optional<List<Map.Entry<String, SecondQueryOutputData>>> query2Result =
                        new SecondQueryResolver("i61448-query2", hazelcastInstance, bikeIMap, stationsAmount)
                            .resolve();

                if (query2Result.isPresent())
                    ClientUtils.writeQuery2(outPath, query2Result.get());
                else{
                    logger.error("Error en la ejecucion de la query2");
                    clearAndExit(bikeIMap, stationIMap, hazelcastInstance, true);
                }

            }
            default -> {
                logger.error("Query invalida");
                System.exit(1);
            }
        }

        logger.info("Fin del trabajo map/reduce");

        // ----------------------------------------

        clearAndExit(bikeIMap, stationIMap, hazelcastInstance,false);
    }
    private static void clearAndExit(IMap<Integer, Bike> bikeIMap, IMap<Integer, Station> stationIMap, HazelcastInstance hazelcastInstance, boolean error) {
        bikeIMap.clear();
        stationIMap.clear();
        hazelcastInstance.getDistributedObjects().forEach(DistributedObject::destroy);
        HazelcastClient.shutdownAll();
        System.exit(error ? 1 : 0);
    }
}

