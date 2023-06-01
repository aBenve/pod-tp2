package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.Collators.OrderStationsByTripsOrAlphabetic;
import ar.edu.itba.pod.tp2.Combiner.SimpleFirstQueryCombinerFactory;
import ar.edu.itba.pod.tp2.Keypredicates.OnlyMembersPredicate;
import ar.edu.itba.pod.tp2.Mappers.OnlyMemberBikesMapper;
import ar.edu.itba.pod.tp2.Models.Bike;
import ar.edu.itba.pod.tp2.Models.TravelIdStationsAndMember;
import ar.edu.itba.pod.tp2.Reducers.CountReducerFactory;
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

public class FirstQueryResolver implements QueryResolver<Map.Entry<String, Integer>>{


    private final HazelcastInstance hazelcastInstance;
    private final String jobName;
    private final IMap<TravelIdStationsAndMember,Bike> fromMap;


    public FirstQueryResolver(String jobName, HazelcastInstance hazelcastInstance, IMap<TravelIdStationsAndMember,Bike> fromMap){
        this.jobName = jobName;
        this.hazelcastInstance = hazelcastInstance;
        this.fromMap = fromMap;

    }

    @Override
    public Optional<List<Map.Entry<String, Integer>>> resolve() {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
        Job<TravelIdStationsAndMember, Bike> job = jobTracker.newJob(KeyValueSource.fromMap(fromMap));

        JobCompletableFuture<List<Map.Entry<String, Integer>>> future = job
                .keyPredicate(new OnlyMembersPredicate())
                .mapper(new OnlyMemberBikesMapper())
                .combiner(new SimpleFirstQueryCombinerFactory())
                .reducer(new CountReducerFactory())
                .submit(new OrderStationsByTripsOrAlphabetic());
        try {
            List<Map.Entry<String, Integer>> resultMap = future.get();
            return Optional.of(resultMap);
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
