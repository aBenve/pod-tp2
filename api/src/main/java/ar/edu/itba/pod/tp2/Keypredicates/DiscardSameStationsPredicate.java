package ar.edu.itba.pod.tp2.Keypredicates;

import ar.edu.itba.pod.tp2.Models.Bike;
import ar.edu.itba.pod.tp2.Models.TravelIdStationsAndMember;
import com.hazelcast.mapreduce.KeyPredicate;

public class DiscardSameStationsPredicate implements KeyPredicate<TravelIdStationsAndMember> {
    @Override
    public boolean evaluate(TravelIdStationsAndMember travelIdStationsAndMember) {
        return ! travelIdStationsAndMember.origin().equals(travelIdStationsAndMember.destination());
    }
}
