package ar.edu.itba.pod.tp2.Reducers;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


public class TakeFastestTravelReducerFactory implements ReducerFactory<String, SecondQueryOutputData, SecondQueryOutputData> {
    @Override
    public Reducer<SecondQueryOutputData, SecondQueryOutputData> newReducer(String s) {
        return new TakeFastestTravel();
    }
    private static class TakeFastestTravel extends Reducer<SecondQueryOutputData, SecondQueryOutputData> {
        private volatile Double maxSpeed;
        private volatile SecondQueryOutputData secondQueryOutputData;

        @Override
        public void beginReduce() {
            maxSpeed = 0.0;
        }

        @Override
        public void reduce(SecondQueryOutputData secondQueryOutputData) {
            if (secondQueryOutputData.velocity() > maxSpeed) {
                maxSpeed = secondQueryOutputData.velocity();
                this.secondQueryOutputData = secondQueryOutputData;
            }
        }

        @Override
        public SecondQueryOutputData finalizeReduce() {
            return secondQueryOutputData;
        }
    }
}
