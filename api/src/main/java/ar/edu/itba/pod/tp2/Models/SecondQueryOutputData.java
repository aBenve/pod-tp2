package ar.edu.itba.pod.tp2.Models;

import java.time.LocalDate;

public class SecondQueryOutputData {

    private final String destination;
    private final Double distance;
    private final Double velocity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public SecondQueryOutputData(String destination, Double distance, Double velocity, LocalDate startDate, LocalDate endDate) {
        this.destination = destination;
        this.distance = distance;
        this.velocity = velocity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getVelocity() {
        return velocity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
