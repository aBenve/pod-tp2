package ar.edu.itba.pod.tp2.client.utils;

import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class ClientUtils {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ClientUtils.class);

    public static Optional<String> getProperty(InputProperty label, Supplier<String> errorMessage){
        String property = System.getProperty(label.getLabel());
        if(property == null){
            logger.error(errorMessage.get());
            return Optional.empty();
        }
        return Optional.of(property);
    }
}