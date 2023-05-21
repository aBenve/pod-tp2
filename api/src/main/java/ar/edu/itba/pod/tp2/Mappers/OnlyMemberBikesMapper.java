package ar.edu.itba.pod.tp2.Mappers;

import ar.edu.itba.pod.tp2.Models.Bike;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class OnlyMemberBikesMapper implements Mapper<Integer, Bike, String, Integer> {
    @Override
    public void map(Integer number, Bike bike, Context<String, Integer> context) {
        if(bike.isMember()){
            context.emit(bike.getOrigin().toString(), 1);
        }
    }
}
