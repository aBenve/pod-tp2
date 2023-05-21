package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;
import java.time.LocalDate;

public record DestinationAndDates(Integer destinationPK, LocalDate startDate, LocalDate endDate) implements Serializable {

}
