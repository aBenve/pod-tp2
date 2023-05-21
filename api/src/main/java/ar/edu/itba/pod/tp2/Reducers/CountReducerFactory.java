package ar.edu.itba.pod.tp2.Reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class CountReducerFactory implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(String s) {
        return new CountReducer();
    }

    private static class CountReducer extends Reducer<Integer, Integer> {
        private volatile int count;

        @Override
        public void beginReduce() {
            count = 0;
        }

        @Override
        public void reduce(Integer number) {
            count += number.intValue();
        }

        @Override
        public Integer finalizeReduce() {
            return count;
        }
    }
}
