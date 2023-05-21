package ar.edu.itba.pod.tp2.Models;

import java.util.Date;

public class Bike {

    private Date startDateTime; // Fecha y hora de inicio del viaje
    private Date endDateTime; // Fecha y hora de fin del viaje
    private Number origin; // Numero de la estación de origen
    private Number destination; // Numero de la estación de destino
    private boolean isMember; // Indica si el viaje fue realizado por un miembro del sistema

    public Bike(Date startDateTime, Date endDateTime, Number origin, Number destination, boolean isMember) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.origin = origin;
        this.destination = destination;
        this.isMember = isMember;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
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
}
