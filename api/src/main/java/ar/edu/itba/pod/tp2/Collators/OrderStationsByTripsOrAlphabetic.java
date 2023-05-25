package ar.edu.itba.pod.tp2.Collators;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class OrderStationsByTripsOrAlphabetic implements Collator<Map.Entry<String, Integer>, Map<String, Integer>> {
    @Override
    public Map<String, Integer> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        SortedSet<Map.Entry<String, Integer>> sortedSet = new TreeSet<>(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> t1, Map.Entry<String, Integer> t2) {
                int tripsComparison = t1.getValue().compareTo(t2.getValue());
                if (tripsComparison == 0) {
                    return t1.getKey().compareTo(t2.getKey());
                }
                return tripsComparison;
            }
        });

        iterable.forEach(sortedSet::add);

        // Create a map with the sorted iterable
        Map<String, Integer> map = new LinkedHashMap<>();
        sortedSet.forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return map;
    }
}
