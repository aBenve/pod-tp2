package ar.edu.itba.pod.tp2.client.utils;

import java.util.List;
import java.util.Optional;

public interface QueryResolver<T> {

    public Optional<List<T>> resolve();

}
