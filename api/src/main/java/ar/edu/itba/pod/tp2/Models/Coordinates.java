package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @param latitude  Latitud
 * @param longitude Longitud
 */
public record Coordinates(double latitude, double longitude) implements Serializable {

    public Double distanceTo(Coordinates other) {
        double latitudeDifference = Math.toRadians(other.latitude - this.latitude);
        double longitudeDifference = Math.toRadians(other.longitude - this.longitude);
        double SQRTSegment = Math.sqrt(
                Math.pow(
                        Math.sin(latitudeDifference / 2)
                        , 2
                ) + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) * Math.pow(
                        Math.sin(longitudeDifference / 2)
                        , 2
                )
        );
        int EARTH_RADIUS = 6371;
        return 2 * EARTH_RADIUS * Math.asin(SQRTSegment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

}
