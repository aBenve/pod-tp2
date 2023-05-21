package ar.edu.itba.pod.tp2.Mappers;

import ar.edu.itba.pod.tp2.Models.Bike;
import ar.edu.itba.pod.tp2.Models.DestinationAndDates;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class StationsPKAndDatesMapper implements Mapper<Integer, Bike, Integer, DestinationAndDates> {
    @Override
    public void map(Integer key, Bike value, Context<Integer, DestinationAndDates> context) {
        context.emit(
                value.getOrigin(),
                new DestinationAndDates(
                        value.getDestination(),
                        value.getStartDateTime(),
                        value.getEndDateTime()
                )
        );
    }
}
