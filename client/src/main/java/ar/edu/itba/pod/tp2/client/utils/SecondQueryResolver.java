package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.Collators.OrderStationsByVelocityOrAlphabetic;
import ar.edu.itba.pod.tp2.Mappers.StationsNamesWithDatesAndDistanceMapper;
import ar.edu.itba.pod.tp2.Models.Bike;
import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import ar.edu.itba.pod.tp2.Reducers.TakeFastestTravelReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SecondQueryResolver implements QueryResolver<Map.Entry<String, SecondQueryOutputData>>{

    private final HazelcastInstance hazelcastInstance;
    private final String jobName;
    private final IMap<Integer,Bike> fromMap;

    private final Integer N;
    public SecondQueryResolver(String jobName, HazelcastInstance hazelcastInstance, IMap<Integer, Bike> fromMap, Integer N){
        this.jobName = jobName;
        this.hazelcastInstance = hazelcastInstance;
        this.fromMap = fromMap;
        this.N = N;

    }
    @Override
    public Optional<List<Map.Entry<String, SecondQueryOutputData>>> resolve() {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("i61448-query2");
        Job<Integer, Bike> job = jobTracker.newJob(KeyValueSource.fromMap(fromMap));

        JobCompletableFuture<List<Map.Entry<String, SecondQueryOutputData>>> future = job
                .mapper(new StationsNamesWithDatesAndDistanceMapper())
                .reducer(new TakeFastestTravelReducerFactory())
                .submit(new OrderStationsByVelocityOrAlphabetic(this.N));

        try {
            List<Map.Entry<String, SecondQueryOutputData>> resultMap = future.get();
            return Optional.of(resultMap);
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
