package ar.edu.itba.pod.tp2.Mappers;

import ar.edu.itba.pod.tp2.Models.Bike;
import ar.edu.itba.pod.tp2.Models.Station;
import ar.edu.itba.pod.tp2.Models.TravelIdStationsAndMember;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serializable;

public class OnlyMemberBikesMapper implements Mapper<TravelIdStationsAndMember, Bike, String, Integer>, HazelcastInstanceAware, Serializable {

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(TravelIdStationsAndMember travelIdStationsAndMember, Bike bike, Context<String, Integer> context) {

        Station origin = (Station) hazelcastInstance.getMap("i61448-station-map").get(bike.getOrigin());
        if (origin == null) {
            throw new RuntimeException("Origin station not found");
        }
        // Esta condicion se paso al predicate
        /*
            if(bike.isMember()){
                context.emit(origin.getName(), 1);
            }
         */
        context.emit(origin.getName(), 1);

    }
    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
