package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Bike implements Serializable {

    private LocalDate startDateTime; // Fecha y hora de inicio del viaje
    private LocalDate endDateTime; // Fecha y hora de fin del viaje
    private Number origin; // Numero de la estación de origen
    private Number destination; // Numero de la estación de destino
    private boolean isMember; // Indica si el viaje fue realizado por un miembro del sistema

    public Bike(LocalDate startDateTime, LocalDate endDateTime, Number origin, Number destination, boolean isMember) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.origin = origin;
        this.destination = destination;
        this.isMember = isMember;
    }



    public LocalDate getStartDateTime() {
        return startDateTime;
    }

    public LocalDate getEndDateTime() {
        return endDateTime;
    }

    public Number getOrigin() {
        return origin;
    }

    public Number getDestination() {
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
