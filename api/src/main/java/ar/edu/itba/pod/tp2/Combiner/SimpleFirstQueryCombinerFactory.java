package ar.edu.itba.pod.tp2.Combiner;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class SimpleFirstQueryCombinerFactory implements CombinerFactory<String, Integer, Integer> {
    @Override
    public Combiner<Integer, Integer> newCombiner(String s) {
        return new SimpleFirstQueryCombiner();
    }

    private class SimpleFirstQueryCombiner extends Combiner<Integer, Integer> {

        private int count;

        @Override
        public void combine(Integer integer) {
            count += integer;
        }

        @Override
        public Integer finalizeChunk() {
            return count;
        }

        @Override
        public void reset() {
            count = 0;
        }

    }
}
