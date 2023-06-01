package ar.edu.itba.pod.tp2.Keypredicates;

import ar.edu.itba.pod.tp2.Models.TravelIdStationsAndMember;
import com.hazelcast.mapreduce.KeyPredicate;

public class OnlyMembersPredicate implements KeyPredicate<TravelIdStationsAndMember> {
    @Override
    public boolean evaluate(TravelIdStationsAndMember travelIdStationsAndMember) {
        return travelIdStationsAndMember.member();
    }
}
