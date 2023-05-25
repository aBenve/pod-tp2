package ar.edu.itba.pod.tp2.Mappers;

import ar.edu.itba.pod.tp2.Models.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class StationsNamesWithDatesAndDistanceMapper implements Mapper<Integer, Bike, String, SecondQueryOutputData>, HazelcastInstanceAware {


    private IMap<Integer, Station> stationIMap;

    @Override
    public void map(Integer key, Bike value, Context<String, SecondQueryOutputData> context) {

        Station origin = stationIMap.get(value.getOrigin());
        if (origin == null) {
            throw new RuntimeException("Origin station not found");
        }

        Station destination = stationIMap.get(value.getDestination());

        if (destination == null) {
            throw new RuntimeException("Destination station not found");
        }

        // If the origin and destination are the same, we don't want to emit anything
        if(origin.equals(destination)){
            return;
        }

        double distance = origin.getCoordinates().distanceTo(destination.getCoordinates());

        Duration time = Duration.between(value.getStartDateTime(), value.getEndDateTime());

        double speed = distance / time.toSeconds();


        speed = Math.round(speed * 100.0) / 100.0;
        distance = Math.round(distance * 100.0) / 100.0;

        context.emit(
                origin.getName(),
                new SecondQueryOutputData(
                        destination.getName(),
                        distance,
                        speed,
                        value.getStartDateTime(),
                        value.getEndDateTime()
                )
        );
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.stationIMap = hazelcastInstance.getMap("i61448-station-map");
    }
}
