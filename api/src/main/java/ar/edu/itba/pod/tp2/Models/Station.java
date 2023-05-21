package ar.edu.itba.pod.tp2.Models;

public class Station {

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
}
