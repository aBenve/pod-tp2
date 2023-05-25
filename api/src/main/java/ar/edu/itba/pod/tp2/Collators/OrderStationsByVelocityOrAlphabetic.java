package ar.edu.itba.pod.tp2.Collators;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class OrderStationsByVelocityOrAlphabetic implements Collator<Map.Entry<String, SecondQueryOutputData>, Map<String, SecondQueryOutputData>> {
    @Override
    public Map<String, SecondQueryOutputData> collate(Iterable<Map.Entry<String, SecondQueryOutputData>> iterable) {
        // Sort this iterable by velocity or alphabetic order
        SortedSet<Map.Entry<String, SecondQueryOutputData>> sortedSet = new TreeSet<>(new Comparator<Map.Entry<String, SecondQueryOutputData>>() {
            @Override
            public int compare(Map.Entry<String, SecondQueryOutputData> t1, Map.Entry<String, SecondQueryOutputData> t2) {
                int velocityComparison = t2.getValue().velocity().compareTo(t1.getValue().velocity());
                if (velocityComparison == 0) {
                    return t1.getKey().compareTo(t2.getKey());
                }
                return velocityComparison;
            }
        });

        iterable.forEach(sortedSet::add);

        // Create a map with the sorted iterable
        Map<String, SecondQueryOutputData> map = new LinkedHashMap<>();
        sortedSet.forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return map;
    }
}
