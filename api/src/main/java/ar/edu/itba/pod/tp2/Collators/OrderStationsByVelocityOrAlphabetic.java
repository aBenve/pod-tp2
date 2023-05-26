package ar.edu.itba.pod.tp2.Collators;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Collator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class OrderStationsByVelocityOrAlphabetic implements Collator<Map.Entry<String, SecondQueryOutputData>, List<Map.Entry<String, SecondQueryOutputData>>>, Serializable {
    private final Integer n;

    public OrderStationsByVelocityOrAlphabetic(Integer n) {
        this.n = n;
    }
    @Override
    public List<Map.Entry<String, SecondQueryOutputData>> collate(Iterable<Map.Entry<String, SecondQueryOutputData>> iterable) {
        List<Map.Entry<String, SecondQueryOutputData>> list = new ArrayList<>();
        iterable.forEach(list::add);

        list.sort((t1, t2) -> {
            int velocityComparison = t2.getValue().velocity().compareTo(t1.getValue().velocity());
            if (velocityComparison == 0) {
                return t2.getKey().compareTo(t1.getKey());
            }
            return velocityComparison;
        });

        return list.stream().limit(n).collect(Collectors.toList());
    }
}
