package ar.edu.itba.pod.tp2.Collators;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class OrderStationsByVelocityOrAlphabetic implements Collator<Map.Entry<String, SecondQueryOutputData>, SortedSet<Map.Entry<String, SecondQueryOutputData>>> {
    @Override
    public SortedSet<Map.Entry<String, SecondQueryOutputData>> collate(Iterable<Map.Entry<String, SecondQueryOutputData>> iterable) {
        SortedSet<Map.Entry<String, SecondQueryOutputData>> sortedSet = new TreeSet<>(new Comparator<Map.Entry<String, SecondQueryOutputData>>() {
            @Override
            public int compare(Map.Entry<String, SecondQueryOutputData> t1, Map.Entry<String, SecondQueryOutputData> t2) {
                int velocityComparison = t2.getValue().velocity().compareTo(t1.getValue().velocity());
                if (velocityComparison == 0) {
                    return t2.getKey().compareTo(t1.getKey());
                }
                return velocityComparison;
            }
        });

        iterable.forEach(sortedSet::add);
        return sortedSet;
    }
}
