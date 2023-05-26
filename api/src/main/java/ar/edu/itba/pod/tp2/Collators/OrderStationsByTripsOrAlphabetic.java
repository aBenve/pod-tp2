package ar.edu.itba.pod.tp2.Collators;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Collator;

import java.util.*;
import java.util.stream.Collectors;

public class OrderStationsByTripsOrAlphabetic implements Collator<Map.Entry<String, Integer>, List<Map.Entry<String, Integer>>> {
    @Override
    public List<Map.Entry<String, Integer>> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>();

        iterable.forEach(list::add);

        list.sort((t1, t2) -> {
            int tripsComparison = t2.getValue().compareTo(t1.getValue());
            if (tripsComparison == 0) {
                return t2.getKey().compareTo(t1.getKey());
            }
            return tripsComparison;
        });

        return list;
    }
}
