package ar.edu.itba.pod.tp2.Collators;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class OrderStationsByTripsOrAlphabetic implements Collator<Map.Entry<String, Integer>, SortedSet<Map.Entry<String, Integer>>> {
    @Override
    public SortedSet<Map.Entry<String, Integer>> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        SortedSet<Map.Entry<String, Integer>> sortedSet = new TreeSet<>(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> t1, Map.Entry<String, Integer> t2) {
                int tripsComparison = t2.getValue().compareTo(t1.getValue());
                if (tripsComparison == 0) {
                    return t2.getKey().compareTo(t1.getKey());
                }
                return tripsComparison;
            }
        });

        iterable.forEach(sortedSet::add);
        return sortedSet;
    }
}
