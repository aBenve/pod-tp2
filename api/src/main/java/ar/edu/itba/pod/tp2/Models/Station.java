package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;

public class Station implements Serializable {

    private Number id; // Numero de la estación
    private String name; // Nombre de la estación
    private Coordinates coordinates; // Coordenadas de la estación

    public Station(Number id, String name, Coordinates coordinates) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public Number getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
