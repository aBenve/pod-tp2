package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;
import java.time.LocalDate;

public record SecondQueryOutputData(String destination, Double distance, Double velocity, LocalDate startDate,
                                    LocalDate endDate) implements Serializable {

}
