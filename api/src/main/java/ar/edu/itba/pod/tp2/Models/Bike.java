package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Bike implements Serializable {

    private LocalDateTime startDateTime; // Fecha y hora de inicio del viaje
    private LocalDateTime endDateTime; // Fecha y hora de fin del viaje
    private Integer origin; // Numero de la estación de origen
    private Integer destination; // Numero de la estación de destino
    private boolean isMember; // Indica si el viaje fue realizado por un miembro del sistema

    public Bike(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer origin, Integer destination, boolean isMember) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.origin = origin;
        this.destination = destination;
        this.isMember = isMember;
    }



    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Integer getOrigin() {
        return origin;
    }

    public Integer getDestination() {
        return destination;
    }

    public boolean isMember() {
        return isMember;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", origin=" + origin +
                ", destination=" + destination +
                ", isMember=" + isMember +
                '}';
    }
}
